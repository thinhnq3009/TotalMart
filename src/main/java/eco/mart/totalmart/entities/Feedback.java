package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;


@Getter
@Setter
@Entity
@Table(name = "Feedback", schema = "dbo")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderDetailId", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "startRate", columnDefinition = "tinyint not null")
    private Short startRate;

    @Nationalized
    @Column(name = "message", nullable = false, length = 1000)
    private String message;

//    @OneToMany(mappedBy = "feedback")
//    private Set<Image> images = new LinkedHashSet<>();

}