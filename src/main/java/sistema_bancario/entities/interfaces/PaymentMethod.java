package sistema_bancario.entities.interfaces;

import sistema_bancario.entities.Transaction;
import sistema_bancario.entities.enums.TransactionTypeEnum;
import sistema_bancario.entities.interfaces.methods.TransactionDataDTO;

public interface PaymentMethod {
    TransactionTypeEnum getTransactionType();

    Transaction processPayment(TransactionDataDTO transactionData);
}
