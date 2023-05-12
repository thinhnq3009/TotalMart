package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.UserService;
import eco.mart.totalmart.services.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ServletContext servletContext;

    @GetMapping("/login")
    String login() {
        return "user/pages/signin";
    }

    @GetMapping("/account/login")
    String loginError(@RequestParam("error") String error, Model model) {
        notificationService.addError(error);
        return "user/pages/signin";
    }


    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("user", new User());
        return "user/pages/signup";
    }

    @PostMapping("/login")
    String login(User user, Model model, HttpServletRequest request) {
//
//        logger.warn("user: " + user);
//
//
//        try {
//            User userLogin = userService.login(user);
//            notificationService.addInfo("Đăng nhập thành công");
//            request.getSession().setAttribute("userLogin", userLogin);
//
//        } catch ( EntityNotFoundException e) {
//            logger.error(e.getMessage());
//            notificationService.addError(e.getMessage());
//            notificationService.render(model);
//            return "user/pages/signin";
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return "user/pages/signin";
//        }
//
        return "direct:/";
    }

    @PostMapping("/register")
    String register(User user, Model model) {

        try {
            userService.register(user);
            notificationService.addInfo("Đăng ký thành công");
        } catch (ConstraintViolationException e) {
            logger.error(e.getMessage());
            notificationService.addError(e.getMessage());
            notificationService.render(model);
            return "user/pages/signup";
        }

        notificationService.render(model);

        return "redirect:/account/login";
    }

}
