package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user-manager")
public class UserManagerController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public String showTableUser(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/dashboard/users";
    }

}
