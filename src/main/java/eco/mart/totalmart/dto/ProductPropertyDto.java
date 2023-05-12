package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.ProductProperty} entity
 */
public record ProductPropertyDto(Long id, PropertyDto properties, String value) implements Serializable {
}