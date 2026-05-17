package sistema_bancario.services.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sistema_bancario.entities.Account;

import java.math.BigDecimal;

public class TransactionValidations {
    private static final Logger log = LoggerFactory.getLogger(TransactionValidations.class);

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    protected void validateAmount(BigDecimal amount) {
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

    protected void validateEnoughBalance(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            log.warn(
                "event=transaction_validation_failed reason=insufficient_balance accountId={} currentBalance={} requestedAmount={}",
                account.getId(),
                account.getBalance(),
                amount
            );

            throw new IllegalArgumentException(
                "Não há saldo suficiente."
            );
        }
    }

    protected void validateEnoughCredit(Account account, BigDecimal amount) {
        if (account.getAvailableLimit().compareTo(amount) < 0) {
            log.warn(
                "event=transaction_validation_failed reason=insufficient_credit accountId={} availableCredit={} requestedAmount={}",
                account.getId(),
                account.getAvailableLimit(),
                amount
            );

            throw new IllegalArgumentException(
                "Não há crédito suficiente."
            );
        }
    }
}
