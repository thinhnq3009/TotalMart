package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.services.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class ApiUsingForTest {

    @Autowired
    CategoryGroupService groupService;

    @GetMapping("/top3")
    List<CategoryGroup> getCategoryGroups() {
        return groupService.getTop3PublicGroups();
    }
}
