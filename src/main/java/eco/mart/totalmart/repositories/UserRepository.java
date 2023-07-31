package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);
}