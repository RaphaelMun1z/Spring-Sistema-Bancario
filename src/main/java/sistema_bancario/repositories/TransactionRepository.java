package sistema_bancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_bancario.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
