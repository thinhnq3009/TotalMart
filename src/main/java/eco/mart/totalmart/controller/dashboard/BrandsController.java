package eco.mart.totalmart.controller.dashboard;

import eco.mart.totalmart.controller.BaseController;
import eco.mart.totalmart.entities.Brand;
import eco.mart.totalmart.requests.PageableRequest;
import eco.mart.totalmart.services.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/admin/brands")
@AllArgsConstructor
public class BrandsController extends BaseController {

    private final BrandService brandService;

    @RequestMapping("/all")
    public String index(
            Model model,
            PageableRequest request
    ) {

        Page<Brand> brands = brandService.getGrands(request);
        model.addAttribute("brands", brands);

        return "user/dashboard/brands";
    }

    @GetMapping("/new")
    public String addBrands(
            Model model
            ) {
        model.addAttribute("brand", new Brand());

        return "user/dashboard/add-brand";
    }

    @PostMapping("/new")
    public String addBrands(
            Brand brand,
             @RequestParam(required = false, value = "imgPoster")MultipartFile imgPoster
            ) {
       Optional<Brand> brandOptional = brandService.save(brand, imgPoster);

         if (brandOptional.isPresent()) {
              return "redirect:/admin/brands/all";
         }

        return "user/dashboard/add-brand";
    }

}
