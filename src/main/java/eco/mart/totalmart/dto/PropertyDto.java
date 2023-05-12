package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Property} entity
 */
public record PropertyDto(Long id, String name, Boolean canClassify, Boolean canFilter,
                          Boolean isImportant) implements Serializable {
}