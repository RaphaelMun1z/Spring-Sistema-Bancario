package sistema_bancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_bancario.entities.users.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCpf(String cpf);
}
