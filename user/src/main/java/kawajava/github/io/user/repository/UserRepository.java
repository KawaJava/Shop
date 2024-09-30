package kawajava.github.io.user.repository;

import kawajava.github.io.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByHash(String hash);
    Optional<User> findByActivationToken(String token);
    @Modifying
    @Query("UPDATE User u SET u.isActive = true, u.activationToken = null, u.activationTokenDate = null WHERE u.id = :userId")
    void activateUser(@Param("userId") Long userId);


}
