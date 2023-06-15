package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}