package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;


    @Autowired
    NotificationService notificationService;
    @GetMapping("/{id}")
    public String getOrder(
            @PathVariable("id") Long id,
            Model model
    ) {


        Optional<Order> order = orderService.findById(id);

        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "user/pages/order-detail";
        } else {
            notificationService.addError("Order not found");
            notificationService.render(model);
            return "redirect:/";
        }

    }


}
