package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}