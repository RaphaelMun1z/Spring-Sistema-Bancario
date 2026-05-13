package sistema_bancario.dtos.res;

import sistema_bancario.entities.Account;
import sistema_bancario.entities.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResDTO (
        String id,
        TransactionTypeEnum type,
        BigDecimal amount,
        Instant moment,
        Account senderAccount,
        Account receiverAccount
){}
