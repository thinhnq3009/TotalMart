package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {


    @Autowired
    CategoryService categoryService;


    @GetMapping("/get")
    ResponseEntity<ResponseObject> findCategoriesByIdOrName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slug
    ) {
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("error")
                .build();


        if (slug == null && name == null) {
            return responseObject.toResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<Category> categories;

        if (slug != null && name != null) {
            categories = categoryService.findByIdOrName(slug, name);
        } else if (slug != null) {
            categories = categoryService.findById(slug)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        } else {
            categories = categoryService.findByName(name)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }

        return responseObject.toBuilder()
                .data(categories)
                .status("success")
                .build()
                .toResponseEntity();

    }


    @DeleteMapping("/delete")
    ResponseEntity<ResponseObject> deleteGroup(@RequestParam("id") String id) {
        return categoryService.delete(id)
                .map(category -> ResponseObject
                        .builder()
                        .data(category)
                        .status("success")
                        .action("delete")
                        .message("Category deleted")
                        .build()
                        .toResponseEntity())
                .orElse(
                        ResponseObject
                                .builder()
                                .message("Category not found")
                                .status("error")
                                .build()
                                .toResponseEntity(HttpStatus.NOT_FOUND)
                );
    }

    @PutMapping("/restore")
    ResponseEntity<ResponseObject> restoreGroup(@RequestParam("id") String id) {
        return categoryService.restore(id)
                .map(category -> ResponseObject
                        .builder()
                        .data(category)
                        .status("success")
                        .message("Category restored")
                        .build()
                        .toResponseEntity())
                .orElse(
                        ResponseObject
                                .builder()
                                .message("Category not restored")
                                .status("error")
                                .build()
                                .toResponseEntity(HttpStatus.NOT_FOUND)
                );
    }


}
