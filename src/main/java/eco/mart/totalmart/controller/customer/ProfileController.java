package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Order;
import eco.mart.totalmart.module.CustomPage;
import eco.mart.totalmart.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    private final OrderService orderService;


}
