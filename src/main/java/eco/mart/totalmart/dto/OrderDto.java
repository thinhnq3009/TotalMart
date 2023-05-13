package eco.mart.totalmart.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Order} entity
 */
public record OrderDto(Long id, UserDto user, AddressDto address, VoucherDto voucher, Instant timeCreated,
                       Instant timeConfirmed, Instant timeComplete, Integer shippingFee) implements Serializable {
}