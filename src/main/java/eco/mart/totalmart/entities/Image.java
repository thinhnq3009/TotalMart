package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Image", schema = "dbo")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "feedbackId")
    @JsonIgnore
    private Feedback feedback;

    @Nationalized
    @Column(name = "url", nullable = false, length = 1000)
    private String url;

}