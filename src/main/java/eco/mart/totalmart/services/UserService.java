
package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        return userRepository.findByUsername(getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseGet(() ->
                        userRepository
                                .findByEmail(username).
                                orElseThrow(
                                        () -> new UsernameNotFoundException("Username or email not found")
                                )
                );
    }



}
