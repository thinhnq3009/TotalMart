package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.enums.OrderStatus;
import eco.mart.totalmart.enums.PaymentMethod;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.OrderService;
import eco.mart.totalmart.services.UserService;
import eco.mart.totalmart.services.VnpService;
import eco.mart.totalmart.vnpay.VNPayGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController extends BaseController {

    private final OrderService orderService;

    private final UserService userService;

    private final NotificationService notificationService;

    private final VnpService vnpService;

    // Hiển thị trang order detail
    @GetMapping("/{id}")
    public String getOrder(
            @PathVariable("id") Long id,
            Model model
    ) {


        Optional<Order> order = orderService.findById(id);

        User user = userService.getUser();

        if (order.isPresent()) {
            if (order.get().getUser().getId().equals(user.getId())
                    || user.isAdmin()) {
                model.addAttribute("order", order.get());
                notificationService.render(model);
                return "user/pages/order-detail";
            }
        }
        notificationService.addError("Order not found");
        return "redirect:/";


    }

    // Thanh toán đơn hàng VN Pay
    @GetMapping("/pay/{id}")
    public String payOrder(
            @PathVariable("id") Long id
    ) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isEmpty()) {
            notificationService.addError("Order not found");
            return "redirect:/order/" + id;
        }

        Order order = orderOpt.get();
        if (!orderService.authenticUser(order)) {
            notificationService.addError("Order not yours");
            return "redirect:/order/" + id;
        }

        if (order.getPaymentMethod() != PaymentMethod.VN_PAY) {
            notificationService.addError("Payment method not supported");
            return "redirect:/order/" + id;
        }

        if (order.getExpiredPayTime() == null ||
                order.getExpiredPayTime().before(new Timestamp(System.currentTimeMillis()))) {

            VNPayGenerator generator = vnpService.getGenerator(order);
            order.setLinkToPay(generator.generateUrl());
            order.setExpiredPayTime(new Timestamp(generator.getExpiredTime().getTime()));
            orderService.save(order);

            return "redirect:" + order.getLinkToPay();
        }

        return "redirect:" + order.getLinkToPay();
    }

    // Xát nhận đơn hàng đã nhận
    @GetMapping("/confirm-received/{id}")
    public String confirmReceived(
            @PathVariable("id") Long id
    ) {
        Optional<Order> orderOpt = orderService.findById(id);
        User user = userService.getUser();
        if (orderOpt.isEmpty()) {
            notificationService.addError("Order not found");
            return "redirect:/order/" + id;
        }

        Order order = orderOpt.get();

        if (orderService.authenticUser(orderOpt.get())
                || user.isAdmin()) {

            if (order.getStatus() != OrderStatus.DELIVERING) {
                notificationService.addError("Order not in shipping status");

            } else {

                order.setTimeComplete(new Timestamp(System.currentTimeMillis()));
                orderService.save(order);
                notificationService.addSuccess("Confirm received successfully");
            }
        }
        return "redirect:/order/" + id;
    }
}
