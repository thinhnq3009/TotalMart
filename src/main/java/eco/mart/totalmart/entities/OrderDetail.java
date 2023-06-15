package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OrderDetail", schema = "dbo")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(name = "sellPrice", nullable = false)
    private Long sellPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Transient
    private int amount;

    public int getAmount() {
        return (int) (quantity * sellPrice);
    }

    //
//    @OneToMany(mappedBy = "orderDetail")
//    private Set<Feedback> feedbacks = new LinkedHashSet<>();

}