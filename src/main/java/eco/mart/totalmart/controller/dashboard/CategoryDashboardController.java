package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.CategoryService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class CategoryDashboardController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CategoryDashboardController.class);

    @Autowired
    CategoryGroupService groupService;

    @Autowired
    CategoryService categoryService;

    @ModelAttribute("groups")
    public List<CategoryGroup> getAllGroups() {
        return groupService.findAll();
    }


    /**
     *
     * @param s number of page
     * @param q number of row
     * @return
     */
    @GetMapping("/show")
    public String showCategoryInTable(
            @RequestParam(required = false) Integer s,
            @RequestParam(required = false) Integer q,
            Model model
    ) {

        model.addAttribute("categories", categoryService.findAllNotDeleted());

        return "user/dashboard/categories";
    }

    @GetMapping("/new")
    String getAllCategoriesBrands(Model model) {
        model.addAttribute("category", new Category());
        return "user/dashboard/add-category";
    }

    @GetMapping("/edit/{id}")
    String editCategory(
            @PathVariable String id,
            Model model
    ) {

        Optional<Category> categoryOptional = categoryService.findById(id);

        if (categoryOptional.isPresent()) {
            model.addAttribute("category", categoryOptional.get());
            return "user/dashboard/add-category";
        }

        return "redirect:/admin/categories/show";
    }

    @PostMapping("/new")
    String addNewCategory(
            Category category,
            @RequestParam(required = false, value = "imgPoster") MultipartFile imgPoster
    ) {

        Optional<Category> categoryOptional = categoryService.save(category, imgPoster);

        if (categoryOptional.isPresent()) {
            return "redirect:/admin/categories/show";
        }


        return "user/dashboard/add-category";
    }

}
