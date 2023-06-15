package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eco.mart.totalmart.enums.DiscountType;
import eco.mart.totalmart.enums.OrderStatus;
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

    @Column(name = "paymentMethodId", nullable = false)
    private Long paymentMethodId  = 1L;

    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "voucherId")
    private Voucher voucher;

    @Column(name = "timeCreated", nullable = false)
    private Timestamp timeCreated = Timestamp.from(Instant.now());

    @Column(name = "timeConfirmed")
    private Timestamp timeConfirmed;

    @Column(name = "timeComplete")
    private Timestamp timeComplete;

    @Column(name = "timeCanceled")
    private Timestamp timeCanceled;


    @Column(name = "shippingFee", nullable = false)
    private Long shippingFee;

    @Column(name = "note", nullable = false)
    private String note;


    @Transient
    private String status ;

    @Transient
    private Long discount;

    @Transient
    private Integer grandTotal;

    public Integer getTotalBill() {
        return orderDetails.stream().map(OrderDetail::getAmount).reduce(0, Integer::sum);
    }

    public Long getGrandTotal() {
        return getTotalBill() + shippingFee - getDiscount();
    }


    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Long getDiscount() {
        if (voucher == null) {
            return 0L;
        }
        if (voucher.getType() == DiscountType.PERCENTAGE) {
            return voucher.getDiscount() * getTotalBill() / 100;
        } else {
            return voucher.getDiscount();
        }
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getStatus() {
        //    PENDING,
        //    CONFIRMED,
        //    COMPLETED,
        //    CANCELLED
        if (timeConfirmed != null) {
            return "Confirmed";
        } else if (timeCanceled != null) {
            return "Cancelled";
        } else if (timeComplete != null) {
            return "Completed";
        } else {
            return "Pending";
        }


    }

}