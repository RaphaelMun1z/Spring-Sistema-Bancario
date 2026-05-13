package sistema_bancario.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction withdraw(BigDecimal amount, String accountId) {
        if(!isValidAmount(amount)) {
            throw new IllegalArgumentException("O valor a ser sacado não é válido.");
        }

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada."));

        if(!hasEnoughBalance(account, amount)) {
            throw new IllegalArgumentException("Não há saldo suficiente.");
        }

        account.withdraw(amount);

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionTypeEnum.WITHDRAW);
        transaction.setMoment(Instant.now());

        Transaction transactionRes = transactionRepository.save(transaction);
        return transactionRes;
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean hasEnoughBalance(Account account, BigDecimal amount) {
        return account.getBalance().compareTo(amount) >= 0;
    }
}
