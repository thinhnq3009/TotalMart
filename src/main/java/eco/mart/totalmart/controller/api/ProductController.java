package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    ResponseEntity<ResponseObject> getAllProduct() {
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .action("get")
                        .message("Get product success")
                        .data(productService.findAll())
                        .build()
                
        );
    }

}
