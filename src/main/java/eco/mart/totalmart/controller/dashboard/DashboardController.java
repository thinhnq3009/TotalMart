package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DashboardController extends BaseController {

    @Autowired
    OrderService orderService;


    @RequestMapping("/admin/dashboard")
    String home(
            Model model
    ) {
        List<Integer> years = orderService.getYearHaveOrder();


        model.addAttribute("incomeInMonth", orderService.getTotalIncomeInMonth(-1, -1));
        model.addAttribute("totalOrderInMonth", orderService.getOrderInMonth(-1, -1).size());
        model.addAttribute("years", years);


        return "user/dashboard/index";
    }

}
