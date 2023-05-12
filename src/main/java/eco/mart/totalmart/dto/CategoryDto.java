package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Category} entity
 */
public record CategoryDto(Long id, CategoryGroupDto categoryGroup, String name, String poster) implements Serializable {


}