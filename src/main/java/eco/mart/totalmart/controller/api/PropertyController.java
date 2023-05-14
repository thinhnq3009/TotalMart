package eco.mart.totalmart.controller.api;


import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.ProductProperty;
import eco.mart.totalmart.entities.Property;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.repositories.PropertyRepository;
import eco.mart.totalmart.services.ProductPropertyService;
import eco.mart.totalmart.services.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {


    private Logger logger = LoggerFactory.getLogger(PropertyController.class);
    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    ProductService productService;

    @Autowired
    private ProductPropertyService productPropertyService;


    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addPropertyToProduct(Property property,
                                                               @RequestParam("productId") Long productId,
                                                               @RequestParam("value") String value
    ) {

        String message;
        String action;
        Optional<Product> productOptional;

        // Check is empty
        if (property.getName().isBlank() || value.isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new ResponseObject(
                            "Property name or value is empty",
                            "error",
                            null));
            // Handel update if property is exists
        } else if (propertyRepository.existsPropertyByName(property.getName().trim())) {

            Property oldProperty = propertyRepository.findByName(property.getName());

            productOptional = productPropertyService.upsertPropertyToProduct(productId, oldProperty, value);

            message = "Update property [%s] successful with value [%s] ".formatted(
                    property.getName(),
                    value
            );

            action = "update";

            // Handel create property if not exists
        } else {

            Property newProperty = propertyRepository.save(property);

            productOptional = productPropertyService.upsertPropertyToProduct(productId, newProperty, value);

            message = "Create property [%s] successful with value [%s] ".formatted(
                    property.getName(),
                    value
            );

            action = "create";

        }

        return productOptional
                .map(product -> ResponseEntity.ok()
                        .body(new ResponseObject(
                                message,
                                "success",
                                action,
                                product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject(
                                "Product not found",
                                "error",
                                null
                        )));


    }

    @DeleteMapping("/delete")
    ResponseEntity<ResponseObject> removeProductProperty(
            @RequestParam(required = false) Long productPropertyId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long propertyId
    ) {

        if (productPropertyId == null && (propertyId == null || productId == null)) {
            return ResponseObject.builder()
                    .message("Product property id or property id and product id is required")
                    .build()
                    .toResponseEntity(HttpStatus.BAD_REQUEST);
        }


        Optional<ProductProperty> productPropertyOptional;

        if (productPropertyId != null) {
            productPropertyOptional = productPropertyService.findById(productPropertyId);
            productPropertyService.remove(productPropertyId);
        } else {
            productPropertyOptional = Optional.of(
                    productPropertyService
                            .findByProductIdAndPropertyId(productId, propertyId)
                            .get(0));
            productPropertyService.removeAllProductProperty(productId, propertyId);
        }

        return productPropertyOptional.map(productProperty -> ResponseObject
                        .builder()
                        .message("Remove property successful")
                        .action("remove")
                        .data(productProperty.getProduct())
                        .build()
                        .toResponseEntity(HttpStatus.OK)
                )
                .orElseGet(
                        () -> ResponseObject
                                .builder()
                                .message("Product property not found")
                                .status("error")
                                .build()
                                .toResponseEntity(HttpStatus.NOT_FOUND)
                );


    }
}
