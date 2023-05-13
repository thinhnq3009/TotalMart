package eco.mart.totalmart.controller.admin;

import eco.mart.totalmart.entities.*;
import eco.mart.totalmart.repositories.*;
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
public class AdminProductController {

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);


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

    @ModelAttribute("categories")
    List<Category> getCategories() {
        return categoryRepository.findAll();
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

        return "admin/pages/all-product";
    }


    // Get form create new product
    @GetMapping("/create")
    String getForm(Model model) {

        model.addAttribute("product", new Product());

        return "admin/pages/create-product";
    }

    // Do create new product
    @PostMapping("/create")
    String doCreate(
            Model model,
            @RequestParam("imgPoster") MultipartFile imgPosterMF,
            @RequestParam("previewImage") MultipartFile[] previewImageMF,
            Product productDto) {

        Product product = productService.upsert(productDto, imgPosterMF, previewImageMF);

        logger.info("Created/Updated product: [%s] %s".formatted(product.getId(), product.getName()));
        notificationService.addSuccess("Created product successfully");
        notificationService.render(model);
        return "admin/pages/create-product";
    }

    @GetMapping("/edit/{id}")
    String doEdit(@PathVariable Long id, Model model) {

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "admin/pages/create-product";
        } else {
            return "redirect:/admin/product/create";
        }

    }


    @GetMapping("/edit-properties/{id}")
    String editProperties(@PathVariable Long id, Model model) {

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            model.addAttribute("product", product.get());
        } else {
            return "redirect:/admin/products/create";
        }

        return "admin/pages/edit-properties-product";
    }
}
