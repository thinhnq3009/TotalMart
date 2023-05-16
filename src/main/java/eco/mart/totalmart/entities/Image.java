package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Builder
@Table(name = "Image", schema = "dbo")
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Image{");
        sb.append("id=").append(id);
        sb.append(", product=").append(product);
        sb.append(", feedback=").append(feedback);
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}