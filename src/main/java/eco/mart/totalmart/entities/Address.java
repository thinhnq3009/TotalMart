package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user;

    @Nationalized
    @Column(name = "receiverName", nullable = false, length = 150)
    private String receiverName;

    @Nationalized
    @Column(name = "receiverPhone", nullable = false, length = 20)
    private String receiverPhone;

    @Nationalized
    @Column(name = "province", nullable = false, length = 200)
    private String provinceName;

    @Column(name = "provinceId", nullable = false)
    private Integer provinceId;

    @Nationalized
    @Column(name = "district", nullable = false, length = 200)
    private String districtName;

    @Column(name = "districtId", nullable = false)
    private Integer districtId;

    @Nationalized
    @Column(name = "commune", nullable = false, length = 200)
    private String communeName;

    @Column(name = "communeId", nullable = false)
    private Integer communeId;

    @Nationalized
    @Column(name = "detail", nullable = false, length = 1000)
    private String detail;

    @Transient
    private String fullAddress;


//    @OneToMany(mappedBy = "address")
//    private Set<Order> orders = new LinkedHashSet<>();


    public String getFullAddress() {
        return detail + ", " + communeName + ", " + districtName + ", " + provinceName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", receiverName='").append(receiverName).append('\'');
        sb.append(", receiverPhone='").append(receiverPhone).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", provinceId=").append(provinceId);
        sb.append(", districtName='").append(districtName).append('\'');
        sb.append(", districtId=").append(districtId);
        sb.append(", communeName='").append(communeName).append('\'');
        sb.append(", communeId=").append(communeId);
        sb.append(", detail='").append(detail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}