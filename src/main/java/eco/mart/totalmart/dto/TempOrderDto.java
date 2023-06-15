package eco.mart.totalmart.dto;

import eco.mart.totalmart.entities.OrderDetail;
import eco.mart.totalmart.entities.User;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link eco.mart.totalmart.entities.Order}
 */
@Builder
@Data
public class TempOrderDto implements Serializable {
    Long id;
    User user;
    Timestamp timeCreated;
    List<OrderDetail> orderDetails;
}