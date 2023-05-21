package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.entities.Image;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.ImageService;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

    private final Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    final
    ServletContext servletContext;

    final
    ImageService imageService;

    public ImageRestController(ServletContext servletContext, ImageService imageService) {
        this.servletContext = servletContext;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    ResponseEntity<ResponseObject> uploadImage(@RequestParam("file") MultipartFile file) {

        UploadHandler handler = new UploadHandler(file);
        try {
            handler.setSubFolder("images");
            handler.initFilename();
            handler.save(servletContext);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


        return ResponseObject
                .builder()
                .message("Upload success")
                .status("success")
                .action("upload")
                .data(handler.getImage())
                .build()
                .toResponseEntity();
    }

    @GetMapping("/all")
    ResponseEntity<ResponseObject> getAllImage() {
        return ResponseObject
                .builder()
                .message("Get all image success")
                .status("success")
                .action("get")
                .data(imageService.findAll())
                .build()
                .toResponseEntity();
    }

    @GetMapping("/get")
    ResponseEntity<ResponseObject> getImage(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long id
    ) {
        ResponseObject responseObject = ResponseObject.builder()
                .action("get")
                .message("Image not found")
                .status("error")
                .build();

        if (productId != null) {
            return responseObject.toBuilder()
                    .data(imageService.findByProductId(productId))
                    .message("Get image success")
                    .status("success")
                    .build()
                    .toResponseEntity()
                    ;
        } else if (id != null) {
            return imageService.findById(id)
                    .map(img -> responseObject.toBuilder()
                            .data(img)
                            .message("Get image success")
                            .status("success")
                            .build()
                            .toResponseEntity()
                    )
                    .orElse(responseObject.toResponseEntity());
        } else {
            return responseObject.toResponseEntity();
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity<ResponseObject> deleteImage(@RequestParam String url) {

        Optional<Image> imageOptional = imageService.findByUrl(url);


        if (imageOptional.isPresent()) {
            try {
                imageService.delete(imageOptional.get());
                return ResponseObject
                        .builder()
                        .message("Delete image success")
                        .status("success")
                        .action("delete")
                        .build()
                        .toResponseEntity();
            } catch (Exception e) {
                return ResponseObject
                        .builder()
                        .message("Delete image failed")
                        .status("error")
                        .action("delete")
                        .build()
                        .toResponseEntity();
            }
        } else {
            return ResponseObject
                    .builder()
                    .message("Image not found")
                    .status("error")
                    .action("delete")
                    .build()
                    .toResponseEntity();
        }



    }

}
