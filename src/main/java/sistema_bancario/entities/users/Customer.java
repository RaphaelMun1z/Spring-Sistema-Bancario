package sistema_bancario.entities.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import sistema_bancario.entities.Account;
import sistema_bancario.entities.enums.UserRole;

import java.time.LocalDate;

@Entity
@Table(name = "tb_customers")
public class Customer extends User {
    @Column(unique = true, nullable = false)
    private String cpf;

    private LocalDate birthDate;

    @OneToOne(mappedBy = "customer")
    private Account account;

    public Customer() {
        super();
    }

    public Customer(
            String id,
            String name,
            String phone,
            String email,
            String password,
            String cpf,
            LocalDate birthDate
    ) {
        super(
                id,
                name,
                phone,
                email,
                password,
                UserRole.CUSTOMER
        );

        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
