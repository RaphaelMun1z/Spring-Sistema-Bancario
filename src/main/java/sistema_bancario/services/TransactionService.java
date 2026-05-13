package sistema_bancario.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sistema_bancario.controllers.exceptions.ResourceExceptionHandler;
import sistema_bancario.dtos.res.TransactionResDTO;
import sistema_bancario.entities.Account;
import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.repositories.AccountRepository;
import sistema_bancario.repositories.TransactionRepository;
import sistema_bancario.services.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResDTO withdraw(BigDecimal amount, String accountId) {
        log.info(
                "event=withdraw_request accountId={} amount={}",
                accountId,
                amount
        );

        validateAmount(amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn(
                            "event=withdraw_failed reason=account_not_found accountId={}",
                            accountId
                    );

                    return new ResourceNotFoundException("Conta não encontrada.");
                });

        if (!hasEnoughBalance(account, amount)) {
            log.warn(
                    "event=withdraw_failed reason=insufficient_balance accountId={} currentBalance={} requestedAmount={}",
                    accountId,
                    account.getBalance(),
                    amount
            );

            throw new IllegalArgumentException("Não há saldo suficiente.");
        }

        account.withdraw(amount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionTypeEnum.WITHDRAW);
        transaction.setMoment(Instant.now());

        Transaction t = transactionRepository.save(transaction);
        log.info(
                "event=withdraw_success transactionId={} accountId={} amount={} balanceAfter={}",
                t.getId(),
                accountId,
                amount,
                account.getBalance()
        );
        return new TransactionResDTO(t.getId(), t.getType(), t.getAmount(), t.getMoment(), t.getSenderAccount(), t.getReceiverAccount());
    }

    @Transactional
    public TransactionResDTO deposit(BigDecimal amount, String accountId) {
        log.info(
                "event=deposit_request accountId={} amount={}",
                accountId,
                amount
        );

        validateAmount(amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn(
                            "event=deposit_failed reason=account_not_found accountId={}",
                            accountId
                    );

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
        log.info(
                "event=deposit_success transactionId={} accountId={} amount={} balanceAfter={}",
                t.getId(),
                accountId,
                amount,
                account.getBalance()
        );
        return new TransactionResDTO(t.getId(), t.getType(), t.getAmount(), t.getMoment(), t.getSenderAccount(), t.getReceiverAccount());
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void validateAmount(BigDecimal amount) {
        if (!isValidAmount(amount)) {
            log.warn(
                    "event=transaction_validation_failed reason=invalid_amount amount={}",
                    amount
            );

            throw new IllegalArgumentException(
                    "O valor informado deve ser maior que zero."
            );
        }
    }

    private boolean hasEnoughBalance(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) >= 0;
    }
}
