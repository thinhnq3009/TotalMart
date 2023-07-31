package eco.mart.totalmart.controller.rest;


import eco.mart.totalmart.entities.Brand;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/brands")
public class BrandRestController {

    private final BrandService brandService;

    @GetMapping("/get")
    ResponseEntity<ResponseObject> findCategoriesByIdOrName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slug
    ) {
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("error")
                .build();

        List<Brand> brands;

        if (slug == null && name == null) {
            return responseObject.toResponseEntity();
        }

        if (slug != null && name != null) {
            brands = brandService.findByNameOrId(name, slug);
        } else if (slug != null) {
            brands = brandService
                    .findById(slug)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            brands = brandService
                    .findByName(name)
                    .map(List::of)
                    .orElse(List.of());
        }

        return responseObject.toBuilder()
                .data(brands)
                .status("success")
                .build()
                .toResponseEntity();
    }
}
