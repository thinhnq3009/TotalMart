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
@Table(name = "Property", schema = "dbo")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "canClassify", nullable = false)
    private Boolean canClassify = false;

    @Column(name = "canFilter")
    private Boolean canFilter = false;

    @Column(name = "isImportant", nullable = false)
    private Boolean isImportant = false;

//    private String type;
//
//    public String getType() {
//
//
//
//        return type;
//    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Property{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", canClassify=").append(canClassify);
        sb.append(", canFilter=").append(canFilter);
        sb.append(", isImportant=").append(isImportant);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property property)) return false;

        return name.equals(property.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

//    @OneToMany(mappedBy = "properties")
//    private Set<ProductProperty> productProperties = new LinkedHashSet<>();

}