package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}