package eco.mart.totalmart.controller.api;


import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.ProductProperty;
import eco.mart.totalmart.entities.Property;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.repositories.PropertyRepository;
import eco.mart.totalmart.services.ProductPropertyService;
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
    private ProductPropertyService productPropertyService;

    // Create new Property
//    @PostMapping("/create")
//    ResponseEntity<ResponseObject> createProperty(Property property) {
//        ResponseObject responseObject = new ResponseObject();
//        Property newProperty;
//        if (propertyRepository.existsPropertyByName(property.getName())) {
//            newProperty = propertyRepository.findByName(property.getName());
//            responseObject.setMessage("Property is exists");
//        } else if (property.getName().isBlank()) {
//            responseObject.setMessage("Property name is empty.");
//            newProperty = null;
//        } else {
//            newProperty = propertyRepository.saveAndFlush(property);
//            responseObject.setMessage("Create successful");
//        }
//        responseObject.setData(newProperty);
//
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseObject);
//    }
//
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addPropertyToProduct(Property property,
                                                               @RequestParam("productId") Long productId,
                                                               @RequestParam("value") String value
    ) {

        String message;
        String action ;
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

            Property newProperty = propertyRepository.saveAndFlush(property);

           productOptional = productPropertyService.upsertPropertyToProduct(productId, newProperty, value);

              message = "Create property [%s] successful with value [%s] ".formatted(
                      property.getName(),
                      value
                );

              action = "create";

        }

        return productOptional.map(product -> ResponseEntity.ok()
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
            @RequestParam("productPropertyId") Long productPropertyId
    ) {

        Optional<ProductProperty> productPropertyOptional = productPropertyService.removeProperty(productPropertyId);
        return productPropertyOptional.map(productProperty -> ResponseEntity.ok()
                        .body(new ResponseObject(
                                "Remove property successful",
                                "success",
                                productProperty.getProduct())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject(
                                "Property not found",
                                "error",
                                null
                        )));


    }
}
