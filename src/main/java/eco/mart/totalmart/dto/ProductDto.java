package eco.mart.totalmart.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Product} entity
 */
public record ProductDto(Long id,
                         BrandDto brand,
                         String category,
                         String name,
                         String poster,
                         Long importPrice,
                         Long basePrice,
                         Long sellPrice,
                         int initialQuantity,
                         int sold,
                         int height,
                         int width,
                         int length,
                         List<ProductPropertyDto> productProperties,
                         List<ImageDto> images) implements Serializable {
}

