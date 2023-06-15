package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllOrderByTimeCreatedBetween(Date start, Date end, Pageable pageable);


}