package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    List<Cart> findAllByUserId(Long id);

    List<Cart> findAllByUserIdAndIdIn(Long userId, List<Long> cartIds);

    void deleteAllByIdIn(List<Long> cartIds);
}