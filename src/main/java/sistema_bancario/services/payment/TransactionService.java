package sistema_bancario.services.payment;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sistema_bancario.dtos.req.TransactionReqDTO;
import sistema_bancario.dtos.res.TransactionResDTO;
import sistema_bancario.entities.Account;
import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.entities.interfaces.PaymentMethod;
import sistema_bancario.entities.interfaces.methods.CreditCardTransactionDTO;
import sistema_bancario.entities.interfaces.methods.PixTransactionDTO;
import sistema_bancario.entities.interfaces.methods.TransactionDataDTO;
import sistema_bancario.repositories.AccountRepository;
import sistema_bancario.repositories.TransactionRepository;
import sistema_bancario.services.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TransactionService extends TransactionValidations {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final Map<TransactionTypeEnum, PaymentMethod> paymentStrategies;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(List<PaymentMethod> paymentMethods, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.paymentStrategies = paymentMethods.stream().collect(Collectors.toMap(PaymentMethod::getTransactionType, Function.identity()));
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResDTO withdraw(BigDecimal amount, String accountId) {
        log.info("event=withdraw_request accountId={} amount={}", accountId, amount);

        validateAmount(amount);

        Account account = accountRepository.findById(accountId).orElseThrow(() -> {
            log.warn("event=withdraw_failed reason=account_not_found accountId={}", accountId);

            return new ResourceNotFoundException("Conta não encontrada.");
        });

        validateEnoughBalance(account, amount);

        account.withdraw(amount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionTypeEnum.WITHDRAW);
        transaction.setMoment(Instant.now());

        Transaction t = transactionRepository.save(transaction);
        log.info("event=withdraw_success transactionId={} accountId={} amount={} balanceAfter={}", t.getId(), accountId, amount, account.getBalance());
        return new TransactionResDTO(t.getId(), t.getType(), t.getAmount(), t.getMoment(), t.getSenderAccount().getId(), null);
    }

    @Transactional
    public TransactionResDTO deposit(BigDecimal amount, String accountId) {
        log.info("event=deposit_request accountId={} amount={}", accountId, amount);

        validateAmount(amount);

        Account account = accountRepository.findById(accountId).orElseThrow(() -> {
            log.warn("event=deposit_failed reason=account_not_found accountId={}", accountId);

            return new ResourceNotFoundException("Conta não encontrada.");
        });

        account.deposit(amount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(null);
        transaction.setReceiverAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionTypeEnum.DEPOSIT);
        transaction.setMoment(Instant.now());

        Transaction t = transactionRepository.save(transaction);
        log.info("event=deposit_success transactionId={} accountId={} amount={} balanceAfter={}", t.getId(), accountId, amount, account.getBalance());
        return new TransactionResDTO(t.getId(), t.getType(), t.getAmount(), t.getMoment(), null, t.getReceiverAccount().getId());
    }

    @Transactional
    public TransactionResDTO realizePayment(TransactionReqDTO dto) {
        log.info("event=payment_processing_request transactionType={} senderAccountId={} receiverAccountId={} amount={}", dto.type(), dto.senderAccountId(), dto.receiverAccountId(), dto.amount());

        PaymentMethod method = paymentStrategies.get(dto.type());

        if (method == null) {
            log.error("event=payment_processing_failed reason=unsupported_payment_method transactionType={}", dto.type());

            throw new IllegalArgumentException("Método de pagamento não suportado: " + dto.type());
        }

        Account senderAccount = accountRepository.findById(dto.senderAccountId()).orElseThrow(() -> {
            log.warn("event=sender_account_search_failed reason=account_not_found accountId={}", dto.senderAccountId());

            return new ResourceNotFoundException("Conta não encontrada.");
        });

        Account receiverAccount = accountRepository.findById(dto.receiverAccountId()).orElseThrow(() -> {
            log.warn("event=receiver_account_search_failed reason=account_not_found accountId={}", dto.receiverAccountId());

            return new ResourceNotFoundException("Conta não encontrada.");
        });

        TransactionDataDTO transactionData = switch (dto.type()) {
            case PIX -> new PixTransactionDTO(dto.amount(), senderAccount, receiverAccount);
            case CREDIT_CARD -> new CreditCardTransactionDTO(dto.amount(), senderAccount, receiverAccount);
            default ->
                throw new IllegalArgumentException("Não foi possível mapear os dados para o tipo: " + dto.type());
        };

        Transaction t = method.processPayment(transactionData);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction savedTransaction = transactionRepository.save(t);

        log.info("event=payment_processing_success transactionType={} transactionId={}", savedTransaction.getType(), savedTransaction.getId());

        return new TransactionResDTO(savedTransaction.getId(), savedTransaction.getType(), savedTransaction.getAmount(), savedTransaction.getMoment(), savedTransaction.getSenderAccount().getId(), savedTransaction.getReceiverAccount().getId());
    }
}
