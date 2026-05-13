package sistema_bancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_bancario.entities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

}
