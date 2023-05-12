package eco.mart.totalmart.handler;

import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.sqm.mutation.internal.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
public class UploadHandler {


    ServletContext servletContext;


    private final Logger logger = LoggerFactory.getLogger(UploadHandler.class);

    MultipartFile multipartFile;


    String subFolder;

    String filename;

    boolean isSave;


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
            Path folder = Paths.get(servletContext.getRealPath(subFolder));


            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
                logger.warn("Created new directory(s): " + folder.toString());
            }

            File file = new File(folder.toString() + "/" + getFilename());

            multipartFile.transferTo(file);

            setSave(true);

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
        return isSave ? subFolder + "/" + filename : null;
    }



}
