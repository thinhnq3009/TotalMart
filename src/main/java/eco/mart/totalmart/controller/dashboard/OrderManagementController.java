package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.enums.OrderStatus;
import eco.mart.totalmart.module.MyPage;
import eco.mart.totalmart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/order")
public class OrderManagementController extends BaseController {

    @Autowired
    OrderService orderService;

    @GetMapping("/all")
    String home(
            Integer page,
            Integer size,
            Integer dayAgo,
            String status,
            Model model
    ) {

        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        dayAgo = dayAgo == null ? 7 : dayAgo;

        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            orderStatus = null;
        }


        Pageable pageable = PageRequest.of(page, size);
        MyPage<Order> orderPage = orderService.findOrder(pageable, dayAgo, orderStatus);

        model.addAttribute("pageContent", orderPage);
        model.addAttribute("dayAgo", dayAgo);
        model.addAttribute("status", orderStatus);
        model.addAttribute("statuses", OrderStatus.values());

        return "user/dashboard/order-list";
    }
}
