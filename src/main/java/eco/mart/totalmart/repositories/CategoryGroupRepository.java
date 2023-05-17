package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, String> {

    @Query("SELECT g, SUM(od.quantity) AS totalSold "
            + "FROM CategoryGroup g "
            + "JOIN g.categories c "
            + "JOIN c.products p "
            + "JOIN p.orderDetails od "
            + "GROUP BY g "
            + "ORDER BY totalSold DESC "
            + "LIMIT 3")
    List<Object[]> findTopThreeCategoriesByTotalSold();


}