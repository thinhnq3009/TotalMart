package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.repositories.CategoryRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ServletContext servletContext;


    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findByIsDeletedTrue() {
        return categoryRepository.findByIsDeletedTrue();
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> findByIdOrName(String id, String name) {
        return categoryRepository.findByIdOrName(id, name);
    }

    public Optional<Category> upsert(Category category, MultipartFile imgPoster) {

        logger.info("Category: " + category.getId());

        try {

            if (imgPoster != null) {
                UploadHandler handler = new UploadHandler(imgPoster);
                handler.setSubFolder("categories");
                handler.setFilename(category.getId());
                handler.save(servletContext);
                category.setPoster(handler.getRelativePath());
            } else {
                category.setPoster(null);
            }

            return Optional.of(categoryRepository.save(category));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();

    }

    public List<Category> findAllNotDeleted() {
        return categoryRepository
                .findByIsDeletedFalse();
    }

    public List<Category> findAllHaveProduct() {
        return categoryRepository
                .findByIsDeletedFalse()
                .stream()
                .filter(c -> !c.getProducts().isEmpty())
                .collect(Collectors.toList());
    }

    public Optional<Category> delete(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setDeleted(true);
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    public Optional<Category> restore(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setDeleted(false);
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }
}
