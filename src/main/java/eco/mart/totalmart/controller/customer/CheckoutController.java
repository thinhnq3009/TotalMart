package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.entities.Voucher;
import eco.mart.totalmart.enums.PaymentMethod;
import eco.mart.totalmart.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    private final NotificationService notificationService;

    private final CartService cartService;

    private final VoucherService voucherService;

    private final CategoryGroupService groupService;

    private final OrderService orderService;

    @ModelAttribute("groups")
    List<CategoryGroup> getGroups() {
        return groupService.getTop3PublicGroups();
    }

    @ModelAttribute("paymentMethods")
    List<PaymentMethod> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values()).toList();
    }

    @RequestMapping("")
    String checkout(
            @RequestParam(value = "i", required = false) List<Long> cartIds, // List of cart id
            @RequestParam(value = "c", required = false) String voucherCode,
            @RequestParam(value = "d", required = false) Long discount,  // Discount
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
        Long finalDiscount = discount;
        model.addAttribute(
                "totalPriceAfterDiscount",
                voucherOtn
                        .map(v -> totalPrice - finalDiscount)
                        .orElse(totalPrice)
        );
        return "user/pages/shop-checkout";
    }


    @RequestMapping("/payment/vnpay/return")
    String returnAfterPaid(
            HttpServletRequest request,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef
    ) {
        if (responseCode.equals("00")) {

            orderService.updateOrderAfterPaid(txnRef);

            return "redirect:/order/" + txnRef;
        }

        return "redirect:/";
    }



}
