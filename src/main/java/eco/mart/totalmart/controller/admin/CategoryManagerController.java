package eco.mart.totalmart.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories-brands")
public class CategoryManagerController {

    @RequestMapping()
    String getAllCategoriesBrands() {
        return "admin/pages/category-management";
    }


}
