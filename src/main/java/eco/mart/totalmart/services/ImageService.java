package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Image;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.repositories.ImageRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ImageService {

    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    ServletContext servletContext;

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public boolean deleteByUrl(String url) {
        imageRepository.deleteByUrl(url);
        return imageRepository.findByUrl(url).isEmpty();
    }

    private Optional<Image> save(MultipartFile multipartFile, String subFolder, String filename, boolean upToDatabase) {
        try {
            UploadHandler uploadHandler = new UploadHandler(multipartFile);
            uploadHandler.setSubFolder(subFolder);
            if (filename != null) uploadHandler.setFilename(filename);
            uploadHandler.save(servletContext);

            Image image = new Image();
            image.setUrl(uploadHandler.getRelativePath());

            logger.info("Save [%s] success".formatted(uploadHandler.getRelativePath()));

            return Optional.of(
                    upToDatabase
                            ? imageRepository.save(image)
                            : image
            );
        } catch (Exception e) {
            logger.error("Can't save image [%s]".formatted(multipartFile.getOriginalFilename()));
            return Optional.empty();
        }
    }

    public Optional<Image> save(MultipartFile multipartFile, String subFolder) {
        return save(multipartFile, subFolder, null, true);
    }

    public Optional<Image> save(MultipartFile multipartFile, String subFolder, String filename) {
        return save(multipartFile, subFolder, filename, true);
    }

    public List<Image> save(MultipartFile[] multipartFile, String subFolder, String filename) {
        return IntStream.range(0, multipartFile.length).mapToObj(
                i -> save(multipartFile[i], subFolder, filename + i).orElse(null)
        ).collect(Collectors.toList());
    }

    public Optional<Image> saveLocal(MultipartFile multipartFile, String subFolder) {
        return save(multipartFile, subFolder, null, false);
    }

    public Optional<Image> saveLocal(MultipartFile multipartFile, String subFolder, String filename) {
        return save(multipartFile, subFolder, filename, false);
    }

    public List<Image> saveLocal(MultipartFile[] multipartFile, String subFolder, String filename) {
        return IntStream.range(0, multipartFile.length).mapToObj(
                i -> save(multipartFile[i], subFolder, filename + i, false).orElse(null)
        ).collect(Collectors.toList());
    }

    public List<Image>  findByProductId(Long productId) {
        return imageRepository.findByProductId(productId);
    }

    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    public Optional<Image> findByUrl(String url) {
        return imageRepository.findByUrl(url);
    }

    public void delete(Image image) {
        imageRepository.delete(image);
    }
}
