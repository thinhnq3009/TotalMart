package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Brand} entity
 */
public record BrandDto(Long id, String name, String image) implements Serializable {
}