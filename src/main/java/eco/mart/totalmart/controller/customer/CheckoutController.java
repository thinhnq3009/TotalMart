package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.entities.*;
import eco.mart.totalmart.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    NotificationService notificationService;


    @Autowired
    CartService cartService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    CategoryGroupService groupService;

    @Autowired
    PriceService priceService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;


    @ModelAttribute("groups")
    List<CategoryGroup> getGroups() {
        return groupService.getTop3PublicGroups();
    }

    @ModelAttribute("user")
    User getUser() {
        return userService.getUser();
    }

    @RequestMapping("")
    String checkout(
            @RequestParam(value = "i", required = false) List<Long> cartIds,
            @RequestParam(value = "c", required = false) String voucherCode,
            @RequestParam(value = "d", required = false) Long discount,
            Model model
    ) {

        if (cartIds == null || cartIds.isEmpty()) {
            notificationService.addError("Cannot checkout empty cart");
            return "redirect:/cart";
        }

        discount = discount == null ? 0 : discount;

        List<Cart> carts = cartService.findAllByUserIdAndIdIn(cartIds);

        if (carts.isEmpty()) {
            notificationService.addError("Cannot checkout empty cart");
            return "redirect:/cart";
        }

        Optional<Voucher> voucherOtn = voucherService.findByCode(voucherCode);
        Long totalPrice = carts.stream().mapToLong(Cart::getTotal).sum();

        model.addAttribute("cartItems", carts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("voucherCode", voucherCode);
        model.addAttribute("voucher", voucherOtn.orElse(new Voucher()));
        model.addAttribute("discount", discount);
        model.addAttribute("totalPriceAfterDiscount",totalPrice - discount);
        return "user/pages/shop-checkout";
    }


}
