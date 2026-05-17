package sistema_bancario.entities;

import jakarta.persistence.Entity;
import sistema_bancario.entities.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class CreditCardTransaction extends Transaction {
    public CreditCardTransaction() {
    }

    public CreditCardTransaction(String id, TransactionTypeEnum type, BigDecimal amount, Instant moment, Account senderAccount, Account receiverAccount) {
        super(id, type, amount, moment, senderAccount, receiverAccount);
    }
}
