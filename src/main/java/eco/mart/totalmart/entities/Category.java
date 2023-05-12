package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;


@Getter
@Setter
@Entity
@Table(name = "Category", schema = "dbo")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

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

//    @OneToMany(mappedBy = "category")
//    private Set<Product> products = new LinkedHashSet<>();

    @Transient
    private String url;

    public String getUrl() {
        return "%s/%s".formatted(getCategoryGroup().getUrl(), getName().replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\s+", "-"));
    }

    @Override
    public String toString() {
        return "%s [%s]".formatted( getName(),getCategoryGroup().getName());
    }
}