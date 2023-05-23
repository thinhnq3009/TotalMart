package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.isDeleted = true")
    List<Product> findByIsDeletedTrue();

    @Query("select p from Product p where p.isDeleted = false")
    List<Product> findByIsDeletedFalse();
}