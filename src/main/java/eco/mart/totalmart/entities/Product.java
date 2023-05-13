package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.validation.ObjectError;

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

    @Column(name = "sellPrice", nullable = false)
    private Long sellPrice;

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


    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductProperty> productProperties = new ArrayList<>();

    @Transient
    private List<Map<String, Object>> properties = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();


    public int getInventory() {
        return initialQuantity - sold;
    }

    public Long getIncome() {
        return sellPrice - importPrice;
    }

    public Integer getDiscountPercent() {
        return (int) ((1 - (double) sellPrice / basePrice) * 100);
    }

    public List<Map<String, Object>> getProperties() {

        Map<String, List<ProductProperty>> propertyMap = getProductProperties()
                .stream()
                .collect(Collectors.groupingBy(productProperty -> productProperty.getProperties().getName()));

//       return propertyMap.entrySet()
//               .stream()
//               .map(entry -> {
//                   Map<String, Object> map = new HashMap<>();
//                   map.put("name", entry.getKey());
//                   map.put("values", entry.getValue().stream().map(ProductProperty::getValue).collect(Collectors.toList()));
//                   return map;
//               })
//               .collect(Collectors.toList());

        return propertyMap.keySet()
                .stream()
                .map(name -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("info", propertyMap.get(name)
                            .get(0)
                            .getProperties());
                    map.put("properties", propertyMap.get(name)
                            .stream()
                            .map(productProperty -> Map.of(
                                    "productPropertyId",
                                    productProperty.getId(),
                                    "value",
                                    productProperty.getValue()))
                            .collect(Collectors.toList()));
                    return map;
                }).collect(Collectors.toList());


    }


}