package eco.mart.totalmart.controller;

import eco.mart.totalmart.components.GmailSenderService;
import eco.mart.totalmart.entities.User;
import eco.mart.totalmart.services.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.UUID;

@Controller
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {

    private final GmailSenderService gmailSender;

    private final UserService userService;

    @GetMapping("/send-mail/{to}/{message}")
    @ResponseBody
    public String sendMail(
            @PathVariable("to") String to
    ) {

        User user = (User) userService.loadUserByUsername(to);

        String token = UUID.randomUUID().toString();

        user.setResetPasswordToken(token);


        try {
            gmailSender.sendResetPasswordMail(user);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "Đã gửi mail cho " + to ;
    }



}
