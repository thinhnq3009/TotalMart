package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.entities.*;
import eco.mart.totalmart.repositories.*;
import eco.mart.totalmart.services.CategoryGroupService;
import eco.mart.totalmart.services.NotificationService;
import eco.mart.totalmart.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {

    private final Logger logger = LoggerFactory.getLogger(ProductAdminController.class);


    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    ProductService productService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CategoryGroupService groupService;

    @ModelAttribute("categoryGroups")
    List<CategoryGroup> getCategories() {
        return groupService.findAll();
    }

    @ModelAttribute("brands")
    List<Brand> getBrands() {
        return brandRepository.findAll();
    }


    @ModelAttribute("properties")
    List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    // Get all product as table
    @GetMapping("/all")
    String getAllProducts(Model model) {

        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);

        return "user/dashboard/products";
    }


    // Get form create new product
    @GetMapping("/create")
    String getForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("action", "Create");

        return "user/dashboard/add-product";
    }

    // Do create new product
    @PostMapping("/create")
    String doCreate(
            Model model,
            @RequestParam("imgPoster") MultipartFile imgPosterMF,
            @RequestParam(value = "previewImage", required = false) MultipartFile[] previewImageMF,
            @RequestParam(value = "imageNames", required = false) String imageNames,
            Product product) {

        model.addAttribute("action", "Create");

        Product productNew = productService.upsert(product, imgPosterMF, imageNames);

        String action = product.getId() == null ? "Created" : "Updated";

        logger.info("%s product: [%s] %s".formatted(
                action,
                productNew.getId(),
                productNew.getName()));

        notificationService.addSuccess("%s product successfully".formatted(action));
        notificationService.render(model);
        return "redirect:/admin/product/all";
    }

    @GetMapping("/edit/{id}")
    String doEdit(@PathVariable Long id, Model model) {

        model.addAttribute("action", "Update");

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "user/dashboard/add-product";
        } else {
            return "redirect:/admin/product/all";
        }

    }


}
