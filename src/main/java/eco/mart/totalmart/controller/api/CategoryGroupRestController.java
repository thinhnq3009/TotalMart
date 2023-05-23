package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.CategoryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/categories/group")
public class CategoryGroupRestController {

    @Autowired
    CategoryGroupService groupService;

    @GetMapping("/get/{id}")
    ResponseEntity<ResponseObject> getCategories(@PathVariable("id") String id) {
        return ResponseObject
                .builder()
                .data(groupService.findById(id).orElseGet(CategoryGroup::new).getCategories())
                .status("success")
                .build()
                .toResponseEntity();

    }

    @GetMapping("/get")
    ResponseEntity<ResponseObject> findGroupsByIdOrName(
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
        Object groups;

        if (slug != null && name != null) {
            groups = groupService.findByIdOrName(slug, name);
        } else if (slug != null) {
            groups = List.of(groupService.findById(slug).orElseGet(CategoryGroup::new));

        } else {
            groups = List.of(groupService.findByName(name).orElseGet(CategoryGroup::new));
        }


        return responseObject.toBuilder()
                .data(groups)
                .status("success")
                .build()
                .toResponseEntity();
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createGroup(
            @RequestParam String name,
            @RequestParam String slug

    ) {

        CategoryGroup categoryGroup = groupService.insert(slug, name);

        return categoryGroup != null
                ? ResponseObject
                .builder()
                .data(categoryGroup)
                .status("success")
                .build()
                .toResponseEntity()
                : ResponseObject
                .builder()
                .status("error")
                .build()
                .toResponseEntity(HttpStatus.NOT_IMPLEMENTED);

    }

    @PutMapping("/restore")
    ResponseEntity<ResponseObject> restoreGroups(@RequestParam("id") String id) {

        return groupService.restore(id).map(
                categoryGroup -> ResponseObject
                        .builder()
                        .data(categoryGroup)
                        .message("Category group restored")
                        .status("success")
                        .build()
                        .toResponseEntity()
        ).orElseGet(
                () -> ResponseObject
                        .builder()
                        .message("Category group not found")
                        .status("error")
                        .build()
                        .toResponseEntity(HttpStatus.NOT_IMPLEMENTED)
        );
    }

}
