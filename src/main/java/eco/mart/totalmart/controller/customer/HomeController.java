package eco.mart.totalmart.controller.customer;


import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.repositories.CategoryGroupRepository;
import eco.mart.totalmart.repositories.CategoryRepository;
import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {


    @Autowired
    CategoryGroupService groupService;

    @Autowired
    ProductService productService;


    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping({"/", "/home"})
    String home(Model model) {

        model.addAttribute("groups", groupService.getTop3PublicGroups());


        return "user/pages/index-2";
    }


    @ModelAttribute("categoryGroups")
    List<CategoryGroup> getCategoryGroups() {
        return groupService.findAll();
    }

}
