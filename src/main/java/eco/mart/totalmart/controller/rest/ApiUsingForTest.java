package eco.mart.totalmart.controller.rest;

import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class ApiUsingForTest {

    @Autowired
    CategoryGroupService groupService;

    @Autowired
    UserService userService;

    @GetMapping("/user-login")
    ResponseEntity<ResponseObject> getUser() {
        return ResponseObject
                .builder()
                .action("getUser")
                .status("success")
                .data(userService.getUserLoggedIn())
                .build()
                .toResponseEntity();
    }

}
