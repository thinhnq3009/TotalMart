package eco.mart.totalmart.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/detail")
public class DetailController {

    @RequestMapping("/{id}")
    String productDetail() {
        return "user/pages/shop-single";
    }



}
