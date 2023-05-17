package eco.mart.totalmart.controller.api;

import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.services.ImageService;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

    private final Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    @Autowired
    ServletContext servletContext;

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

}
