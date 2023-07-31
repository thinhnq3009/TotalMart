package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.entities.Property;
import eco.mart.totalmart.repositories.ProductRepository;
import eco.mart.totalmart.services.ProductService;
import eco.mart.totalmart.services.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Controller
@RequestMapping("/admin/properties")
public class ProductPropertyController extends BaseController {

    @Autowired
    PropertyService propertyService;
    @Autowired
    ProductService productService;

    @ModelAttribute("properties")
    List<Property> getProperties() {
        return propertyService.findAll();
    }

    @GetMapping("/{productId}")
    String getTablerProperty(
            @PathVariable Long productId,
            Model model) {

        Optional<Product> productOtn = productService.findById(productId);

        if (productOtn.isPresent()) {
            model.addAttribute("product", productOtn.get());
        } else {
            return "redirect:/admin/product/all";
        }

        return "user/dashboard/properties";
    }


}
