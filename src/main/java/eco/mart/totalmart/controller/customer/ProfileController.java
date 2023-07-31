package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    @RequestMapping("")
    public String index() {
        return "user/pages/account-orders";
    }


}
