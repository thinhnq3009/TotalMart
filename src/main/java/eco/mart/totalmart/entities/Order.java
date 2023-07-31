package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eco.mart.totalmart.enums.DiscountType;
import eco.mart.totalmart.enums.OrderStatus;
import eco.mart.totalmart.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "\"Order\"", schema = "dbo")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    @Transient
    private String username;

    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "voucherId")
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "timeCreated", nullable = false)
    private Timestamp timeCreated = Timestamp.from(Instant.now());

    @Column(name = "timeConfirmed")
    private Timestamp timeConfirmed;

    @Column(name = "timeComplete")
    private Timestamp timeComplete;

    @Column(name = "timeCanceled")
    private Timestamp timeCanceled;

    @Column(name = "timeDelivery")
    private Timestamp timeDelivery;  // Giao cho bên vận chuyển


    @Column(name = "shippingFee", nullable = false)
    private Long shippingFee;   // Được tính từ địa chỉ giao hàng

    @Column(name = "note", nullable = false)
    private String note;


    @Transient
    private OrderStatus status;

    // Tiền giảm giá được tính qua Voucher
    @Transient
    private Long discount;

    @Transient
    private Long finalTotal;

    private String linkToPay;

    @Column(name = "isPaid", nullable = false)
    @JsonIgnore
    private boolean paid;

    private Timestamp expiredPayTime;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public boolean isExpiredPayLink() {
        if (paymentMethod == PaymentMethod.COD) {
            return false;
        }
        if (expiredPayTime == null) {
            return false;
        }
        return expiredPayTime.before(new Timestamp(System.currentTimeMillis()));
    }

    public String getLinkToPay() {
        if (paymentMethod == PaymentMethod.VN_PAY && !isExpiredPayLink()) {
                    return linkToPay;
        } else {
            return "/";
        }

    }

    // Tiền tiền hàng chưa tính ship và discount
    public Integer getTotalBill() {
        return orderDetails
                .stream()
                .map(OrderDetail::getAmount)
                .reduce(0, Integer::sum);
    }

    // Tổng tiền phải trả
    public Long getFinalTotal() {
        return getTotalBill()
                + getShippingFee()
                - getDiscount();
    }

    // Chi phí bỏ ra của một đơn hàng
    public Long getCost() {
        return orderDetails
                .stream()
                .mapToLong(od -> od.getProduct().getImportPrice() * od.getQuantity())
                .sum();
    }

//    public String getPaymentStatus() {
//        if (paymentMethod == PaymentMethod.COD) {
//            return getStatusString();
//        } else if (paymentMethod == PaymentMethod.VN_PAY) {
//            return isPaid()
//                    ? "Paid"
//                    : "Waiting for payment";
//        } else {
//            return "Undefined";
//        }
//    }

    public Long getDiscount() {

        if (voucher == null) {
            return 0L;
        }

        if (voucher.getMinApply() > getTotalBill())
            return 0L;

        Long discount;
        if (voucher.getType() == DiscountType.PERCENTAGE) {
            discount = voucher.getDiscount() * getTotalBill() / 100;
        } else {
            discount = voucher.getDiscount();
        }

        return discount > voucher.getMaxDiscount()
                ? voucher.getMaxDiscount()
                : discount;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public OrderStatus getStatus() {
        return status == null
                ? OrderStatus.getStatus(this)
                : status;
    }

    public String getPaymentStatus() {
        if (paymentMethod == PaymentMethod.COD) {
            return getStatusString();
        } else if (paymentMethod == PaymentMethod.VN_PAY) {
            return isPaid()
                    ? "Paid"
                    : "Waiting for payment";
        } else {
            return "Undefined";
        }
    }

    public String getStatusString() {
        return getStatus().getName();
    }

}