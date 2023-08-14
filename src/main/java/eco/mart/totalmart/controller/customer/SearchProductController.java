package eco.mart.totalmart.controller.customer;


import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.enums.FilterProduct;
import eco.mart.totalmart.module.CustomPage;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
@AllArgsConstructor
public class SearchProductController {

    private final NotificationService notificationService;

    private final ProductService productService;

    @GetMapping("")
    public String search(
            @RequestParam String keyword,
            Integer page,
            Integer size,
            String filter,
            Model model
    ) {

        page = page == null ? 0 : page;
        size = size == null ? 12 : size;

        Pageable pageable = Pageable.ofSize(size).withPage(page);


        if (keyword.isEmpty()) {
            notificationService.addError("Vui lòng nhập từ khóa tìm kiếm");
            return "redirect:/";
        }

       CustomPage<Product> products = productService.findAllByNameLike(keyword, pageable);


        model.addAttribute("keyword", keyword);
        model.addAttribute("pageContent", products);
        model.addAttribute("filters", FilterProduct.values());


        return "user/pages/search-result";
    }


}
