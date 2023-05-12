package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Image} entity
 */
public record ImageDto(String url) implements Serializable {
}