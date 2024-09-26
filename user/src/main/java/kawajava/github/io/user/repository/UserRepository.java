package kawajava.github.io.user.repository;

import kawajava.github.io.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
