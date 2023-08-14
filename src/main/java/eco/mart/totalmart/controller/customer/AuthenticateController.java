package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.UserService;
import eco.mart.totalmart.services.NotificationService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AuthenticateController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final NotificationService notificationService;

    private final ServletContext servletContext;

    @GetMapping("/login")
    String login(Model model) {
        notificationService.render(model);
        return "user/pages/signin";
    }




    @GetMapping("/register")
    String register(Model model) {
        model.addAttribute("user", new User());
        return "user/pages/signup";
    }

//    @PostMapping("/login")
//    String login(User user, Model model, HttpServletRequest request) {
//
//        logger.warn("user: " + user.getUsername());
//
//
//        try {
//            User userLogin = userService.login(user);
//            notificationService.addInfo("Đăng nhập thành công");
//            request.getSession().setAttribute("userLogin", userLogin);
//            notificationService.render(model);
//        } catch ( Exception e) {
//            logger.error(e.getMessage());
//            notificationService.addError(e.getMessage());
//            notificationService.render(model);
//            return "user/pages/signin";
//        }
//
//        return "direct:/";
//    }

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

//        notificationService.render(model);

        return "redirect:/account/login";
    }

}
