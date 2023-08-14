package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllOrderByTimeCreatedBetween(Date start, Date end, Sort sort);


    @Query("select YEAR(o.timeCreated) from Order o group by YEAR(o.timeCreated) order by YEAR(o.timeCreated) DESC")
    List<Integer> getYearHaveOrder();

    List<Order> findAllByTimeCreatedBetween(Date start, Date end);

    List<Order> findAllByUserOrderByTimeCreatedDesc(User userLoggedIn, Sort sort);

    List<Order> findAllByUser(User userLoggedIn);
}