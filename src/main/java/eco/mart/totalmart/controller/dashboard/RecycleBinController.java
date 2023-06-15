package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.CategoryService;
import eco.mart.totalmart.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin/recycle")
public class RecycleBinController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryGroupService groupService;

    private final Logger logger = LoggerFactory.getLogger(RecycleBinController.class);


    @ModelAttribute("categoriesDeleted")
    public List<Category> categoriesDeleted() {

        List<Category> categories = categoryService.findByIsDeletedTrue();
        logger.info("Categories deleted: " + categories.size());
        return categories;
    }

    @ModelAttribute("productsDeleted")
    public List<Product> productsDeleted() {
        List<Product> products = productService.findByIsDeletedTrue();
        logger.info("Products deleted: " + products.size());
        return products;
    }

    @ModelAttribute("groupsDeleted")
    public List<CategoryGroup> groupsDeleted() {
        List<CategoryGroup> groups = groupService.findByIsDeletedTrue();
        logger.info("Groups deleted: " + groups.size());
        return groups;
    }



    @GetMapping
    String showRecycleBin() {
        return "user/dashboard/recycle-bin";
    }



}
