package eco.mart.totalmart.services;

import eco.mart.totalmart.dto.TempOrderDto;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.NoPermissionException;
import javax.swing.text.html.Option;
import java.security.Permission;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserService userService;



    public Cart addProductToCart(Product product, Integer quantity) throws UsernameNotFoundException {

        User user = userService.getUser();
        Cart cart;
        Optional<Cart> cartOtn = cartRepository.findByUserIdAndProductId(user.getId(), product.getId());

        // If cart is present, update quantity
        if (cartOtn.isPresent()) {
            cart = cartOtn.get();
            cart.setQuantity(cart.getQuantity() + (quantity > 0 ? quantity : 1));
            cartRepository.save(cart);
        } else {
            cart = Cart
                    .builder()
                    .product(product)
                    .quantity(quantity > 0 ? quantity : 1)
                    .user(user)
                    .build();
        }
        return cartRepository.save(cart);

    }


    public List<Cart> getCartItems() {
        User user = userService.getUser();
        return cartRepository.findAllByUserId(user.getId());
    }

    public Optional<Cart> updateProduct(Long cartId, Integer quantity) throws NoPermissionException {
        Optional<Cart> cartOtn = cartRepository.findById(cartId);


        if (cartOtn.isPresent()) {
            if (!userService.getUser().equals(cartOtn.get().getUser())) {
                throw new NoPermissionException("You are not allowed to update this cart");
            }
            Cart cart = cartOtn.get();
            cart.setQuantity(quantity);
            return Optional.of(cartRepository.save(cart));
        } else {
            return Optional.empty();
        }
    }

    boolean checkUserPermission(Long id) {
        Optional<Cart> cartOtn = cartRepository.findById(id);
        return cartOtn.filter(cart -> userService.getUser().equals(cart.getUser())).isPresent();

    }

    public void removeItem(Long cartId) throws NoPermissionException {
        if (checkUserPermission(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new NoPermissionException("You are not allowed to delete this cart");
        }
    }

//    public TempOrderDto checkout(List<Long> cartIds) {
//        List<Cart> carts = cartRepository.findAllById(cartIds);
//        TempOrderDto tempOrderDto = orderService.initOrder(carts);
//
//        return tempOrderDto;
//    }

    /**
     * Find all carts by id and user
     *
     * @param cartIds
     * @return
     */
    public List<Cart> findAllByUserIdAndIdIn(List<Long> cartIds) {
        return cartRepository.findAllByUserIdAndIdIn(userService.getUser().getId(), cartIds);
    }

    public List<Cart> findByIdIn(List<Long> cartId) {
        return cartRepository.findAllById(cartId);
    }

    public List<Cart> findCartsByIds(List<Long> cartIds) {
        return cartRepository.findAllById(cartIds);
    }

    public void deleteCarts(List<Long> cartIds) {
        cartRepository.deleteAllByIdIn(cartIds);
    }
}
