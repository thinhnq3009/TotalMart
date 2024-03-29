package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
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
public class CategoryDashboardController extends BaseController {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CategoryDashboardController.class);

    @Autowired
    CategoryGroupService groupService;

    @Autowired
    CategoryService categoryService;

    @ModelAttribute("groups")
    public List<CategoryGroup> getAllGroups() {
        return groupService.findAll();
    }


    @GetMapping("/show")
    public String showCategoryInTable(
            Model model
    ) {

        model.addAttribute("categories", categoryService.findAllNotDeleted());

        return "user/dashboard/categories";
    }

    @GetMapping("/new")
    String getFromCreateNewCategory(Model model) {
        model.addAttribute("status", "Create");
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
            model.addAttribute("status", "Update");
            model.addAttribute("category", categoryOptional.get());
            return "user/dashboard/add-category";
        }

        return "redirect:/admin/categories/show";
    }

    @PostMapping("/new")
    String addNewCategory(
            Category category,
            String oldId,
            @RequestParam(required = false, value = "imgPoster") MultipartFile imgPoster
    ) {

        logger.warn(oldId);

        Optional<Category> categoryOptional = categoryService.upsert(category, imgPoster);

        if (categoryOptional.isPresent()) {
            return "redirect:/admin/categories/show";
        }


        return "user/dashboard/add-category";
    }

}
