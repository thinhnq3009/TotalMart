package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "Product", schema = "dbo")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "groupCode")
    private String groupCode;

    @ManyToOne()
    @JoinColumn(name = "brandId")
    private Brand brand;

    @ManyToOne()
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    // Get all category as table same categoryGroup
    @Transient
    private List<Category> categories = new ArrayList<>();

    @Nationalized
    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Nationalized
    @Column(name = "poster", length = 1000)
    private String poster;

    @Column(name = "importPrice", nullable = false)
    private Long importPrice;

    @Column(name = "basePrice", nullable = false)
    private Long basePrice;

    @Column(name = "salePrice", nullable = false)
    private Long salePrice;

    @Transient
    private Integer discountPercent;

    @Transient
    private int inventory;

    @Column(name = "initialQuantity", nullable = false)
    private int initialQuantity;

    @Column(name = "sold", nullable = false)
    private int sold;

    @Transient
    private Long income;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "length", nullable = false)
    private int length;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductProperty> productProperties = new ArrayList<>();

    @Transient
    private List<GroupProperty> properties = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();


    public int getInventory() {
        return initialQuantity - sold;
    }

    public Long getIncome() {
        return salePrice - importPrice;
    }

    public Integer getDiscountPercent() {
        return (int) ((1 - (double) salePrice / basePrice) * 100);
    }

    public List<GroupProperty> getProperties() {

        Map<String, List<ProductProperty>> propertyMap = getProductProperties()
                .stream()
                .collect(Collectors.groupingBy(productProperty -> productProperty.getProperties().getName()));

        return propertyMap.keySet()
                .stream()
                .map(name -> GroupProperty.builder()
                        .name(name)
                        .info(propertyMap.get(name)
                                .get(0)
                                .getProperties())
                        .propertiesValue(propertyMap.get(name))
                        .build())
                .collect(Collectors.toList());


    }

    public List<Category> getCategories() {
        return (category != null && category.getCategoryGroup() != null)
                ? category.getCategoryGroup().getCategories()
                : categories;

    }
}