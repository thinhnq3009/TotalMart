package eco.mart.totalmart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", brand=").append(brand);
        sb.append(", category=").append(category);
        sb.append(", name='").append(name).append('\'');
        sb.append(", poster='").append(poster).append('\'');
        sb.append(", importPrice=").append(importPrice);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", sellPrice=").append(sellPrice);
        sb.append(", inventory=").append(inventory);
        sb.append(", initialQuantity=").append(initialQuantity);
        sb.append(", sold=").append(sold);
        sb.append(", height=").append(height);
        sb.append(", width=").append(width);
        sb.append(", length=").append(length);
        sb.append(", orderDetails=").append(orderDetails);
        sb.append(", productProperties=").append(productProperties);
        sb.append(", images=").append(images);
        sb.append('}');
        return sb.toString();
    }
}