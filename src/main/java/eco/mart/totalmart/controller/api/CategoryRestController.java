package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {

    @Autowired
    CategoryGroupService groupService;

    @GetMapping("/group/{id}")
    ResponseEntity<ResponseObject> getCategories(@PathVariable("id") String id) {

        return ResponseObject
                .builder()
                .data(groupService.findById(id).orElseGet(CategoryGroup::new).getCategories())
                .status("success")
                .build()
                .toResponseEntity();

    }


}
