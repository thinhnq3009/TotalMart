package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.enums.OrderStatus;
import eco.mart.totalmart.module.CustomPage;
import eco.mart.totalmart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            String sortBy,
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

        Sort sort;
        if (sortBy == null || sortBy.isEmpty()) {
            sort = Sort.by("timeCreated").descending();
        } else {
            String property = sortBy;
            boolean isDescending = false;

            if (sortBy.startsWith("!")) {
                property = sortBy.substring(1); // Bỏ ký tự "!" ở đầu
                isDescending = true;
            }

            sort = isDescending ? Sort.by(property).descending() : Sort.by(property).ascending();
        }


        Pageable pageable = PageRequest.of(page, size);
        CustomPage<Order> orderPage = orderService.findOrder(pageable, dayAgo, orderStatus, sort);

        model.addAttribute("pageContent", orderPage);
        model.addAttribute("dayAgo", dayAgo);
        model.addAttribute("status", orderStatus);
        model.addAttribute("statuses", OrderStatus.values());

        return "user/dashboard/order-list";
    }
}
