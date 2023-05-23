package eco.mart.totalmart.handler;

import eco.mart.totalmart.entities.Image;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadHandler {

    private final Logger logger = LoggerFactory.getLogger(UploadHandler.class);
    private final String ROOT = "/public/upload";

    MultipartFile multipartFile;


    String subFolder;

    String filename;

    boolean isSaved;


    public UploadHandler(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public void save(ServletContext servletContext) throws IOException {
        if (!multipartFile.isEmpty()) {

            // Handel rename file
            if (getFilename() == null || getFilename().isEmpty()) {
                setFilename(multipartFile.getOriginalFilename());
            } else {
                String uploadExtension = getExtension(multipartFile.getOriginalFilename());

                if (!getFilename().endsWith(uploadExtension)) {
                    String newExtension = getExtension(getFilename());
                    if (newExtension == null) {
                        setFilename(getFilename() + uploadExtension);
                    } else {
                        logger.warn("File upload have an extension is \"%s\" but has been changed to \"%s\"".formatted(uploadExtension, newExtension));
                    }
                }
            }

            // Folder = realPath +  root + subfolder
            Path folder = Paths.get(servletContext.getRealPath(ROOT + subFolder));


            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
                logger.warn("Created new directory(s): " + folder);
            }

            File file = new File(folder + "/" + getFilename());

            multipartFile.transferTo(file);

            setSaved(true);

        }
    }

    private String validateFolderName(String name) {
        return name.startsWith("/") ? name : "/" + name;
    }

    public void setSubFolder(String subFolder) {
        this.subFolder = validateFolderName(subFolder);
    }

    public String getExtension(String filename) {
        if (filename != null && filename.lastIndexOf(".") != -1) {
            return "." + filename.substring(filename.lastIndexOf(".") + 1);
        }
        return null;
    }

    public String getRelativePath() {
        return isSaved ? ROOT + subFolder + "/" + filename : null;
    }

    public Image getImage() {
        return Image.builder()
                .url(getRelativePath())
                .build();
    }

    public void initFilename() {
        if (getFilename() == null || getFilename().isEmpty()) {
            String randomName= UUID.randomUUID().toString();
            setFilename(randomName + getExtension(multipartFile.getOriginalFilename()));
        }
    }

}
