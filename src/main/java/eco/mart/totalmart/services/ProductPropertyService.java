package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.ProductProperty;
import eco.mart.totalmart.entities.Property;
import eco.mart.totalmart.repositories.ProductPropertyRepository;
import eco.mart.totalmart.repositories.ProductRepository;
import eco.mart.totalmart.repositories.PropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductPropertyService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPropertyRepository productPropertyRepository;


    private Logger logger = LoggerFactory.getLogger(ProductPropertyService.class);
    @Autowired
    private PropertyRepository propertyRepository;

    /**
     * Kiểm tra product đã có property đó hay chưa.
     * Nếu chưa thì add vào, có rồi thì kiểm tra có cho classify không.
     * Có thì add tiếp không có thì update
     *
     * @param productId
     * @param property
     * @param value
     * @return product added property
     */
    public Optional<Product> upsertPropertyToProduct(Long productId, Property property, String value) {
        Optional<Product> productOptional = productRepository.findById(productId);

        // Handel if found product
        if (productOptional.isPresent()) {
            List<ProductProperty> properties
                    = productPropertyRepository
                    .findByProductIdAndPropertiesId(
                            productOptional.get().getId(),
                            property.getId()
                    );
            //if product property exists
            if (!properties.isEmpty()) {
                if (property.getCanClassify()) {
                    addNewProperty(productOptional.get(), property, value);
                } else {
                    ProductProperty productProperty = properties.get(0);
                    productProperty.setValue(value);
                    productPropertyRepository.save(productProperty);
                }
            } else {
                addNewProperty(productOptional.get(), property, value);
            }


        }
        return productOptional;
    }

    private void addNewProperty(Product product, Property property, String value) {
        ProductProperty productProperty = new ProductProperty(product, property);
        productProperty.setValue(value);
        productPropertyRepository.save(productProperty);
    }

//    public Optional<Product> add

    public void remove(Long productPropertyId) {
            productPropertyRepository.deleteById(productPropertyId);
    }

    public void removeAllProductProperty(Long productId, Long propertyId) {
        List<ProductProperty> productProperties = productPropertyRepository.findByProductIdAndPropertiesId(productId, propertyId);
        productPropertyRepository.deleteAll(productProperties);
    }

    public Optional<ProductProperty> findById(Long productPropertyId) {
        return productPropertyRepository.findById(productPropertyId);
    }

//    findByProductIdAndPropertyId
    public List<ProductProperty> findByProductIdAndPropertyId(Long productId, Long propertyId) {
        return productPropertyRepository.findByProductIdAndPropertiesId(productId, propertyId);
    }
}
