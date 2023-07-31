package eco.mart.totalmart.controller.customer;


import eco.mart.totalmart.components.GmailSenderService;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    private final NotificationService notificationService;

    private final UserService userService;

    private final GmailSenderService gmailSender;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/forgot-password")
    String forgotPassword(

    ) {


        return "user/pages/forgot-password";
    }

    @PostMapping("/forgot-password")
    String forgotPassword(
            Model model,
            @RequestParam(value = "username", required = false) String username
    ) {
        // Check input
        if (username == null) {
            notificationService.addError("Username is required");
            notificationService.render(model);
            return "user/pages/forgot-password";
        }


        try {

            User user = (User) userService.loadUserByUsername(username);

            user.generateResetPasswordToken();

            userService.save(user);

            gmailSender.sendResetPasswordMail(user);

            logger.warn("User: " + user.getUsername() + " " + user.getEmail());

            notificationService.addSuccess("Check your email to reset password");
//            notificationService.render(model);
            return "redirect:/account/login";

        } catch (UsernameNotFoundException e) {
            notificationService.addError("Username not found");
            notificationService.render(model);
            return "user/pages/forgot-password";
        } catch (MessagingException | FileNotFoundException e) {
            notificationService.addError("Something went wrong, can't send email");
            notificationService.render(model);
            return "user/pages/forgot-password";
        }


    }


    @GetMapping("/reset-password/{token}")
    public String resetPasswordToken(
            Model model,
            @PathVariable("token") String token
    ) {

        if (userService.validateToken(token)) {
            return "user/pages/reset-password";
        } else {
            notificationService.addError("Invalid token");
            model.addAttribute("title", "Invalid token");
            model.addAttribute("message", "This token is invalid or expired. You can send another request to reset password");
            notificationService.render(model);
            return "user/pages/error-404";
        }
    }

    @PostMapping("/reset-password/{token}")
    public String resetPasswordTokenPost(
            Model model,
            @PathVariable("token") String token,
            @RequestParam("password") String password
    ) {

        if (!userService.validateToken(token)) {
            notificationService.addError("Invalid token");
            model.addAttribute("title", "Invalid token");
            model.addAttribute("message", "This token is invalid or expired. You can send another request to reset password");
            notificationService.render(model);
            return "user/pages/error-404";
        }

        User user =userService.findByResetPasswordToken(token);

        user.setPassword(passwordEncoder.encode(password));
        user.clearResetPasswordToken();
        userService.save(user);

        notificationService.addSuccess("Password changed successfully");
        return "redirect:/account/login";


    }

}
