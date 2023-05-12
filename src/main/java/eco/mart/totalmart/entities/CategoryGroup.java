package eco.mart.totalmart.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "CategoryGroup", schema = "dbo")
public class CategoryGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Nationalized
    @Column(name = "poster", length = 1000)
    private String poster;

    @OneToMany(mappedBy = "categoryGroup")
    private Set<Category> categories = new LinkedHashSet<>();

    @Transient
    private String url;

    public String getUrl() {
        return "/" + getName().replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", "")
                .replaceAll("\\s+", "-").toLowerCase();
    }
}