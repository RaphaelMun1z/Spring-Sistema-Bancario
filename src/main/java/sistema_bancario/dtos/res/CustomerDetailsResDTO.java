package sistema_bancario.dtos.res;

import java.time.LocalDate;

public record CustomerDetailsResDTO(
        String id,
        String name,
        String phone,
        String email,
        String cpf,
        LocalDate birthDate
) {}
