package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


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
    private Instant timeCreated;

    @Column(name = "timeConfirmed")
    private Instant timeConfirmed;

    @Column(name = "timeComplete")
    private Instant timeComplete;

    @Column(name = "shippingFee", nullable = false)
    private Integer shippingFee;

//    @OneToMany(mappedBy = "order")
//    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

}