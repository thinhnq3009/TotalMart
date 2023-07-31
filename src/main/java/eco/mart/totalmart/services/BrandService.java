package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.Brand;
import eco.mart.totalmart.entities.Category;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.repositories.BrandRepository;
import eco.mart.totalmart.requests.PageableRequest;
import jakarta.servlet.ServletContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    private final ServletContext servletContext;

    public Page<Brand> getGrands(PageableRequest request) {
        return brandRepository.findAll(request.toPageable());
    }


    public List<Brand> findByNameOrId(String name, String slug) {
        return brandRepository.findByNameOrId(name, slug);
    }

    public Optional<Brand> findById(String slug) {
        return brandRepository.findById(slug);
    }

    public Optional<Brand> findByName(String name) {
        return brandRepository.findByName(name);
    }

    public Optional<Brand> save(Brand brand, MultipartFile imgPoster) {


        try {

            if (imgPoster != null) {
                UploadHandler handler = new UploadHandler(imgPoster);
                handler.setSubFolder("categories");
                handler.setFilename(brand.getId());
                handler.save(servletContext);
                brand.setImage(handler.getRelativePath());
            } else {
                brand.setImage(null);
            }

            return Optional.of(brandRepository.save(brand));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }
}
