package sistema_bancario.entities.interfaces.methods;

import sistema_bancario.entities.Account;
import sistema_bancario.entities.enums.TransactionTypeEnum;

import java.math.BigDecimal;

public record PixTransactionDTO(
    BigDecimal amount,
    Account senderAccount,
    Account receiverAccount
) implements TransactionDataDTO {
    @Override
    public TransactionTypeEnum type() {
        return TransactionTypeEnum.PIX;
    }
}