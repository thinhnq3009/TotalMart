package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);

    void deleteByUrl(String url);

    Optional<Image> findByUrl(String url);

}