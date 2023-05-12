package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//     List<Product> fin
}