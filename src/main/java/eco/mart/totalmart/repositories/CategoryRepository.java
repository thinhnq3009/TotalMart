package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}