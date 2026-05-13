package sistema_bancario.dtos.req;

import java.time.LocalDate;

public record CustomerReqDTO(
        String name,
        String phone,
        String email,
        String password,
        String cpf,
        LocalDate birthDate
) {}
