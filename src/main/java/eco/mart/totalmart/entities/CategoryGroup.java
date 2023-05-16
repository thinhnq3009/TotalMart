package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
@Table(name = "CategoryGroup", schema = "dbo")
public class CategoryGroup {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Nationalized
    @Column(name = "poster", length = 1000)
    private String poster;

    @OneToMany(mappedBy = "categoryGroup")
    private List<Category> categories = new ArrayList<>();

    @Transient
    private String url;

    @Transient
    private List<Product> allProduct = new ArrayList<>();

    public String getUrl() {
        return "/%s".formatted(getId());
    }

    public List<Product> getAllProduct() {
        return categories.stream()
                .flatMap(category -> category.getProducts().stream())
                .collect(Collectors.toList());
    }
}