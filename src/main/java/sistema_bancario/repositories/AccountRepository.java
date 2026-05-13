package sistema_bancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_bancario.entities.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByCustomerId(String customerId);
}
