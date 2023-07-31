package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eco.mart.totalmart.enums.DefaultData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Entity
@Table(name = "Category", schema = "dbo")
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "categoryGroupId", nullable = false)
    @JsonIgnore
    private CategoryGroup categoryGroup;

    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;


    @Nationalized
    @Column(name = "poster", length = 1000)
    private String poster;

    @Column(name = "isNonActive", nullable = false)
    private boolean isNonActive = false;

    @Column(name = "isDeleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();


    @Transient
    private String url;

    @Transient
    private int productCount;



    public Optional<Category> toOptional() {
        return Optional.of(this);
    }

    public String getUrl() {
        return "/%s/%s".formatted(getCategoryGroup().getId(),getId());
    }

    public int getProductCount() {
        return products.size();
    }

    public String getPoster() {

        return (poster == null || poster.isBlank()) ? DefaultData.CATEGORY_IMAGE.getValue() : poster;

    }
}