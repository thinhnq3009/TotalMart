package eco.mart.totalmart.controller.customer;

import eco.mart.totalmart.handler.UploadHandler;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FilesController {

    @Autowired
    ServletContext servletContext;

    Logger logger = LoggerFactory.getLogger(FilesController.class);

    @GetMapping("/upload")
    String get() {
        return "form";
    }

    @PostMapping("/upload")
    String upload(@RequestParam("poster")MultipartFile multipartFile) throws IOException {

        UploadHandler fileHandler = new UploadHandler(multipartFile);
        fileHandler.setFilename("Poster.jpg");
        fileHandler.setSubFolder("container_poster");
        fileHandler.save(servletContext);
        logger.info("Save file success");

        return "form";
    }
}
