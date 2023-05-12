package eco.mart.totalmart.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {
    @RequestMapping("/admin/dashboard")
    String home() {
        return "admin/pages/index";
    }

}
