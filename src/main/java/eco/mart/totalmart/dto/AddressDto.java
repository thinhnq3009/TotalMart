package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Address} entity
 */
public record AddressDto(Long id, String receiverName, String receiverPhone, String province, String district,
                         String commune, String detail) implements Serializable {
}