package eco.mart.totalmart.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link eco.mart.totalmart.entities.Voucher} entity
 */
public record VoucherDto(Long id, String code, String poster, Long discount, Long minApply, Long maxDiscount,
                         Integer quantity, Boolean isPercentDiscount) implements Serializable {
}