package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    @GetMapping("")
    String cart() {

        return "user/pages/shop-cart";
    }

}
