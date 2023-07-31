package eco.mart.totalmart.enums;

import eco.mart.totalmart.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("Đang chờ xử lý"),
    CONFIRMED ("Đã xác nhận"),
    DELIVERING ("Đang giao hàng"),
    COMPLETED ("Đã giao hàng"),
    CANCELED ("Đã hủy");




    private final String text;

    public static OrderStatus getStatus(Order order) {
        if (order.getTimeCanceled() != null) {
            return CANCELED;
        }
        if (order.getTimeComplete() != null) {
            return COMPLETED;
        }
        if (order.getTimeDelivery() != null) {
            return DELIVERING;
        }
        if (order.getTimeConfirmed() != null) {
            return CONFIRMED;
        }
        if (order.isPaid()) {
            return CONFIRMED;
        }
        return PENDING;
    }

    public String getName() {
        // Title case
        String name = this.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
