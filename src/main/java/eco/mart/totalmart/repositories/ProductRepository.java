package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.isDeleted = true")
    List<Product> findByIsDeletedTrue();

    @Query("select p from Product p where p.isDeleted = false")
    List<Product> findByIsDeletedFalse();

    @Query("select p from Product p where p.category.id = ?1")
    Page<Product> findAllByCategory(String categoryId, Pageable pageable);

    Page<Product> findAllByNameLike(String key, Pageable pageable);
}