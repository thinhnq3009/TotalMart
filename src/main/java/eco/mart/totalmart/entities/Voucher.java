package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eco.mart.totalmart.enums.DiscountType;
import eco.mart.totalmart.exceptions.VoucherException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Voucher", schema = "dbo")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User userId;

    @Nationalized
    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Nationalized
    @Column(name = "poster", length = 1000)
    private String poster;

    @Column(name = "discount", nullable = false)
    private Long discount;

    @Column(name = "minApply", nullable = false)
    private Long minApply;

    @Column(name = "maxDiscount", nullable = false)
    private Long maxDiscount;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType type;

    public void use() throws VoucherException {
        if (quantity > 0) {
            quantity--;
        } else {
            throw new VoucherException("Voucher is out of stock");
        }
    }


//    @OneToMany(mappedBy = "voucher")
//    private Set<Order> orders = new LinkedHashSet<>();

}