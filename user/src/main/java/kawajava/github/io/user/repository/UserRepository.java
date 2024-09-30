package kawajava.github.io.user.repository;

import kawajava.github.io.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByHash(String hash);
    Optional<User> findByActivationToken(String token);

}
