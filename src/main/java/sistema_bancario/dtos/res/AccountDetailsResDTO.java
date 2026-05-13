package sistema_bancario.dtos.res;

import java.math.BigDecimal;

public record AccountDetailsResDTO(
        String id,
        BigDecimal balance,
        String customerCpf
) {
}
