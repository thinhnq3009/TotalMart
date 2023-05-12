package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}