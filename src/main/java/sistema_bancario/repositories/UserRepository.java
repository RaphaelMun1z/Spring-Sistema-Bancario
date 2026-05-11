package sistema_bancario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import sistema_bancario.entities.User;

public interface UserRepository<T extends User> extends JpaRepository<T, String> {
    UserDetails findByEmail(String email);
}