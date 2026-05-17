package sistema_bancario.dtos.req;

import sistema_bancario.entities.enums.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionReqDTO(
        TransactionTypeEnum type,
        BigDecimal amount,
        String senderAccountId,
        String receiverAccountId
) {
}
