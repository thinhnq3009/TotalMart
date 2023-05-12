package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.ProductProperty;
import eco.mart.totalmart.entities.Property;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.repositories.ProductPropertyRepository;
import eco.mart.totalmart.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductPropertyService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPropertyRepository productPropertyRepository;


    private Logger logger = LoggerFactory.getLogger(ProductPropertyService.class);

    public Optional<Product> upsertPropertyToProduct(Long productId, Property property, String value) {
        Optional<Product> productOptional = productRepository.findById(productId);

        // Handel if found product
        if (productOptional.isPresent()) {

            Optional<ProductProperty> productPropertyOptional = productPropertyRepository.findByProductIdAndPropertiesId(productId, property.getId());

            ProductProperty productProperty = productPropertyOptional
                    .orElseGet(() -> new ProductProperty(productOptional.get(), property)
            );

            productProperty.setValue(value);

            productPropertyRepository.save(productProperty);

        }
        return productOptional;


    }

    public Optional<ProductProperty> removeProperty(Long id) {
        Optional<ProductProperty> productPropertyOptional = productPropertyRepository.findById(id);
        if (productPropertyOptional.isPresent()) {
            productPropertyRepository.delete(productPropertyOptional.get());
        } else {
            logger.error("Not found product property have id is " + id);
        }
        return productPropertyOptional;
    }

    public ProductProperty removeProperty(ProductProperty productProperty) {
        productPropertyRepository.delete(productProperty);
        return productProperty;
    }

    public Optional<ProductProperty> removeProperty(Product product, Property property) {
        Optional<ProductProperty> productPropertyOptional = productPropertyRepository.findByProductIdAndPropertiesId(product.getId(), property.getId());
        productPropertyOptional.ifPresent(productProperty -> productPropertyRepository.delete(productProperty));
        return productPropertyOptional;

    }


}
