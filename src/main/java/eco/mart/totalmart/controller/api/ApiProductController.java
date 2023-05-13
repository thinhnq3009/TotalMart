package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ApiProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    ResponseObject getAllProduct() {
        return
                ResponseObject.builder()
                        .action("get")
                        .message("Get product success")
                        .data(productService.findAll())
                        .build();
    }

    @GetMapping("/{id}")
    ResponseObject getProductById( @PathVariable long id) {
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            return ResponseObject.builder()
                    .action("get")
                    .message("Get product success")
                    .data(productOptional.get())
                    .build();
        } else {
            return ResponseObject.builder()
                    .action("get")
                    .message("Product not found")
                    .build();
        }
    }


}
