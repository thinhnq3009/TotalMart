package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.module.CustomPage;
import eco.mart.totalmart.services.CategoryService;
import eco.mart.totalmart.services.ProductService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/show")
    public String category(
            @RequestParam(value = "category", required = false)
            String category,
            @RequestParam(value = "brand", required = false)
            String brand,
            Model model,
            Integer page,
            Integer size
    ) {

        brand = brand == null ? "" : brand;
        category = category == null ? "" : category;

        if (category.isEmpty() && brand.isEmpty()) {
            return "user/pages/error-404";
        }


        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size);

        CustomPage<Product> pageContent ;

        if (category.isEmpty()) {
            pageContent = productService.findByBrand(brand, pageable);
        } else {
            pageContent = productService.findByCategory(category, pageable);
        }

        if (pageContent.isEmpty()) {
            return "user/pages/error-404";
        }

        model.addAttribute("pageContent", pageContent);
        model.addAttribute("category", categoryService.findById(category).orElseGet(Category::new));
        return "user/pages/shop-fullwidth";
    }

}
