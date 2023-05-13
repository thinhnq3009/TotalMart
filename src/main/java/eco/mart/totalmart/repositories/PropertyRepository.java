package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    boolean existsPropertyByName(String name);

    boolean existsByIdAndCanClassify(Long id, boolean canClassify);

    Property findByName(String name);
}