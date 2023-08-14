//package eco.mart.totalmart.controller;
//
//import eco.mart.totalmart.components.GmailSenderService;
//import eco.mart.totalmart.entities.User;
//import eco.mart.totalmart.services.UserService;
//import jakarta.mail.MessagingException;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.FileNotFoundException;
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/demo")
//@AllArgsConstructor
//public class DemoController {
//
//    private final GmailSenderService gmailSender;
//
//    private final UserService userService;
//
//    @GetMapping("/send-mail/{to}/{message}")
//    @ResponseBody
//    public String sendMail(
//            @PathVariable("to") String to
//    ) {
//
//        User user = (User) userService.loadUserByUsername(to);
//
//        String token = UUID.randomUUID().toString();
//
//        user.setResetPasswordToken(token);
//
//
//        try {
//            gmailSender.sendResetPasswordMail(user);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return "Đã gửi mail cho " + to ;
//    }
//
//    @RequestMapping("/demo/1")
//    public String demo1(
//            @RequestParam("name") String name,
//            @RequestParam("age") Integer age
//    ) {
//
//        return "user/pages/demo/demo1";
//    }
//
//    @RequestMapping("/demo/2")
//    public String demo2(
//            Student student
//    ) {
//        return "user/pages/demo/demo2";
//    }
//
//    @RequestMapping("/demo/3")
//    public String demo3(
//            @RequestBody Student student
//    ) {
//        return "user/pages/demo/demo3";
//    }
//
//    public class Student {
//        private String name;
//        private Integer age;
//    }
//
//}
