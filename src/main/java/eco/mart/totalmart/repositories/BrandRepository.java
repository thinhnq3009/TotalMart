package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Brand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String> {
    List<Brand> findByNameOrId(String name, String slug);


    Optional<Brand> findByName(String name);
}