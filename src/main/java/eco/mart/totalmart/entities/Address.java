package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Address", schema = "dbo")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Nationalized
    @Column(name = "receiverName", nullable = false, length = 150)
    private String receiverName;

    @Nationalized
    @Column(name = "receiverPhone", nullable = false, length = 20)
    private String receiverPhone;

    @Nationalized
    @Column(name = "province", nullable = false, length = 200)
    private String province;

    @Nationalized
    @Column(name = "district", nullable = false, length = 200)
    private String district;

    @Nationalized
    @Column(name = "commune", nullable = false, length = 200)
    private String commune;

    @Nationalized
    @Column(name = "detail", nullable = false, length = 1000)
    private String detail;

//    @OneToMany(mappedBy = "address")
//    private Set<Order> orders = new LinkedHashSet<>();

}