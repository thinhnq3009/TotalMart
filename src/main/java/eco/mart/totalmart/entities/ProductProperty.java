package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ProductProperties", schema = "dbo")
public class ProductProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "propertiesId", nullable = false)
    @JsonIgnore
    private Property properties;


    @Nationalized
    @Column(name = "\"value\"", nullable = false, length = 1000)
    private String value;

    public ProductProperty(Product product, Property properties) {
        this.product = product;
        this.properties = properties;
    }

}