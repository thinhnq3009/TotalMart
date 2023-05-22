package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "\"Order\"", schema = "dbo")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @Column(name = "paymentMethodId", nullable = false)
    private Long paymentMethodId;

    @ManyToOne
    @JoinColumn(name = "addressId", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "voucherId", nullable = false)
    private Voucher voucher;

    @Column(name = "timeCreated", nullable = false)
    private Timestamp timeCreated;

    @Column(name = "timeConfirmed")
    private Timestamp timeConfirmed;

    @Column(name = "timeComplete")
    private Timestamp timeComplete;

    @Column(name = "shippingFee", nullable = false)
    private Integer shippingFee;

    public Integer getTotalBill() {
        return orderDetails.stream().map(OrderDetail::getAmount).reduce(0, Integer::sum);
    }

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

}