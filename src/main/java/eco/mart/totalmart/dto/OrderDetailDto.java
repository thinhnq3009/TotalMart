package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.OrderDetail} entity
 */
public record OrderDetailDto(Long id, OrderDto order, ProductDto product, Long sellPrice, Integer quantity,
                             String note) implements Serializable {
}