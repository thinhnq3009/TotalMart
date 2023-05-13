package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
    List<ProductProperty> findByProductId(Long id);


    List<ProductProperty> findByProductIdAndPropertiesId(Long productId, Long propertyId);



//    boolean existsByProductIdAndPropertiesId(Long productId, Long proper)
}