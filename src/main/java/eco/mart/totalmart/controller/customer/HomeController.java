package eco.mart.totalmart.controller.customer;


import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.repositories.CategoryGroupRepository;
import eco.mart.totalmart.repositories.CategoryRepository;
import eco.mart.totalmart.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController extends BaseController {
//
    @Autowired
    NotificationService notificationService;


    @RequestMapping({"/", "/home"})
    String home(Model model) {

        notificationService.render(model);

        return "user/pages/index-2";
    }

}
