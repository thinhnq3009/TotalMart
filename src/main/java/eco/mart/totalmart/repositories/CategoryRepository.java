package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);

    List<Category> findByIdOrName(String id, String name);

    @Query("select c from Category c where c.isDeleted = false")
    List<Category> findByIsDeletedFalse();

}