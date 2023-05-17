package eco.mart.totalmart.controller.admin;

import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.services.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories-brands")
public class CategoryManagerController {

    @Autowired
    CategoryGroupService groupService;

    @ModelAttribute("groups")
    public List<CategoryGroup> getAllGroups() {
        return groupService.findAll();
    }

    @RequestMapping()
    String getAllCategoriesBrands() {
        return "user/dashboard/add-category";
    }


}
