package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/detail")
public class DetailController extends BaseController {

    @Autowired
    ProductService productService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping("/{id}")
    String productDetail(Model model, @PathVariable Long id) {

        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "user/pages/shop-single";
        } else {
            notificationService.addError("Product not found", 5000);
            notificationService.render(model);
            
            return "user/pages/error-404";
        }


    }


}
