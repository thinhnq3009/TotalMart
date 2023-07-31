package eco.mart.totalmart.controller;

import eco.mart.totalmart.entities.Cart;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class BaseController {


    @Autowired
     CategoryGroupService groupService;


    @Autowired
    CategoryService categoryService;


    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;


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

    @ModelAttribute("user")
    User getUser() {
        return userService.getUser() ;
    }

}
