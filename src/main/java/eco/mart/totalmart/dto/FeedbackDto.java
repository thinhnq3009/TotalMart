package eco.mart.totalmart.dto;

import eco.mart.totalmart.entities.OrderDetail;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Feedback} entity
 */
public record FeedbackDto(Long id, OrderDetail orderDetail, Short startRate, String message) implements Serializable {
}