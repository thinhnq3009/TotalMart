
package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> register(User user) throws ConstraintViolationException {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConstraintViolationException(
                    "Username \"%s\" already exists".formatted(user.getUsername())
                    , null
                    , "username");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConstraintViolationException(
                    "Email \"%s\" already exists".formatted(user.getEmail())
                    , null
                    , "email");
        }

        // Using Spring to hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Optional.of(userRepository.save(user));

    }

    public User login(User user) throws EntityNotFoundException {

        User userFound = userRepository.findByUsername(user.getUsername())
                .orElseGet(() -> userRepository.findByEmail(user.getEmail())
                        .orElseThrow(() -> new EntityNotFoundException("Username or email not found")));


        if (passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            return userFound;
        } else {
            throw new EntityNotFoundException("Password incorrect");
        }
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUser() {
        return userRepository.findByUsername(getUsername()).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String typeUsername = username.contains("@") ? "email" : "username";


        return userRepository
                .findByUsername(username)
                .orElseGet(() ->
                        userRepository
                                .findByEmail(username).
                                orElseThrow(
                                        () -> new UsernameNotFoundException("Not found user with %s: %s".formatted(typeUsername, username))
                                )
                );
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public User findByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean validateToken(String token) {
        return userRepository
                .findByResetPasswordToken(token)
                .map(
                        user -> user.getResetPasswordTokenExpiryDate().after(new Date())
                ).orElse(false);
    }
}
