package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @Transient
    private String url;


    public String getUrl() {
        return "%s/%s".formatted(getCategoryGroup().getId(),getId());
    }


}