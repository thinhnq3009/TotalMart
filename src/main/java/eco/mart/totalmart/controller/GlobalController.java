package eco.mart.totalmart.controller;

import eco.mart.totalmart.controller.customer.HomeController;
import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ControllerAdvice
public class GlobalController {


    @Autowired
    CategoryGroupService groupService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private CartService cartService;

    private Logger logger = LoggerFactory.getLogger(HomeController.class);


    @ModelAttribute("groups")
    List<CategoryGroup> getGroups() {
        return groupService.getTop3PublicGroups();
    }

    @ModelAttribute("categories")
    List<Category> getCategoryGroups() {
        return categoryService.findAllHaveProduct();
    }

    @ModelAttribute("categoryGroups")
    List<CategoryGroup> getCategoryGroup() {
        return groupService.findAllNotDeleted();
    }


    @ModelAttribute("cartItems")
    List<Cart> getCartItems() {
        return cartService.getCartItems();
    }



}
