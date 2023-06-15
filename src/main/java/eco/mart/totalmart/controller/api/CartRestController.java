package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.dto.TempOrderDto;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.CartService;
import eco.mart.totalmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/carts")
public class CartRestController {

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;


    @PostMapping("/add")
    ResponseEntity<ResponseObject> addProductToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", required = false) Integer quantity
    ) {


        ResponseObject responseObject = ResponseObject
                .builder()
                .action("addProductToCart")
                .status("error")
                .build();

        Optional<Product> product = productService.findById(productId);

        if (product.isEmpty())
            return responseObject.message("Product not found").toResponseEntity(HttpStatus.NOT_FOUND);

        try {
            cartService.addProductToCart(product.get(), quantity);
            return responseObject
                    .toBuilder()
                    .message("Product added to cart")
                    .data(product.get())
                    .status("success")
                    .build()
                    .toResponseEntity(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return responseObject.message("Please login to add to cart").toResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    ResponseEntity<ResponseObject> updateProduct(
            @RequestParam("cartId") Long cartId,
            @RequestParam("quantity") Integer quantity
    ) {
        ResponseObject responseObject = ResponseObject
                .builder()
                .action("updateProduct")
                .status("error")
                .build();

        try {
            Optional<Cart> cartOtn = cartService.updateProduct(cartId, quantity);
            return cartOtn
                    .map((cart) -> responseObject
                            .toBuilder()
                            .message("Cart updated")
                            .data(cart)
                            .status("success")
                            .build()
                            .toResponseEntity(HttpStatus.OK))
                    .orElseGet(
                            () -> responseObject.message("Cart not found").toResponseEntity(HttpStatus.NOT_FOUND)
                    );
        } catch (NoPermissionException e) {
            return responseObject
                    .message("You don't have permission to update this cart")
                    .toResponseEntity(HttpStatus.OK);
        }


    }


    @PostMapping("/remove")
    ResponseEntity<ResponseObject> deleteProduct(
            @RequestParam("cartId") Long cartId
    ) {
        ResponseObject response = ResponseObject
                .builder()
                .action("deleteCartItem")
                .status("success")
                .message("Cart item deleted")
                .build();

        try {
            cartService.removeItem(cartId);
            return response.toResponseEntity();
        } catch (NoPermissionException e) {
            return response
                    .toBuilder()
                    .status("error")
                    .message(e.getMessage())
                    .build()
                    .toResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return response
                    .toBuilder()
                    .status("error")
                    .message(e.getMessage())
                    .build()
                    .toResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/checkout")
//    ResponseEntity<ResponseObject>  checkout(
//            @RequestParam("cartIds[]") List<Long> cartIds
//    ) {
//
//        TempOrderDto order = cartService.checkout(cartIds);
//
//        return ResponseObject
//                .builder()
//                .data(order)
//                .message("Checkout success")
//                .build().toResponseEntity();
//
//
//    }

}
