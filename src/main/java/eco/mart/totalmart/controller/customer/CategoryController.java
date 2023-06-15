package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.module.MyPage;
import eco.mart.totalmart.services.CartService;
import eco.mart.totalmart.services.CategoryService;
import eco.mart.totalmart.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.model.IModel;

@Controller
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/{subCategory}/{category}")
    public String category(
            @PathVariable("category") String category,
            @PathVariable("subCategory") String subCategory,
            Model model,
            Integer page,
            Integer size,
            String sort

    ) {

        Pageable pageable = PageRequest.of(page == null ? 0 : page, size == null ? 10 : size);
        MyPage<Product> pageContent = productService.findByCategory(category, pageable);


        model.addAttribute("pageContent", pageContent);
        model.addAttribute("category", categoryService.findById(category).orElseGet(Category::new));
        return "user/pages/shop-fullwidth";
    }

}
