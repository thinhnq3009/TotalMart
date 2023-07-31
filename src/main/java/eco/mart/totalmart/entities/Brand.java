package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eco.mart.totalmart.defaultes.DefaultValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Brand", schema = "dbo")
public class Brand {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Nationalized
    @Column(name = "image", length = 1000)
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();

    @Transient
    private Integer totalProduct;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Brand{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Integer getTotalProduct() {
        return products.size();
    }

    public String getImage() {
        return image == null
                ? DefaultValue.DEFAULT_IMG_BRAND
                : image;
    }
}