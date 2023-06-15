package eco.mart.totalmart.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eco.mart.totalmart.entities.Image;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.module.MyPage;
import eco.mart.totalmart.repositories.ImageRepository;
import eco.mart.totalmart.repositories.ProductRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ServletContext servletContext;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByIsDeletedTrue() {
        return productRepository.findByIsDeletedTrue();
    }

    public List<Product> findByIsDeletedFalse() {
        return productRepository.findByIsDeletedFalse();
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Product upsert(Product product, MultipartFile imgPosterMF, String imageNames) {

        logger.info(product.getPoster() + "___");

        // Save product to get product's id
        Product newProduct = productRepository.saveAndFlush(product);

        String folderName = "/images/products/product-%s".formatted(String.valueOf(newProduct.getId()));


        // Save poster image
        if (imgPosterMF != null && !imgPosterMF.isEmpty()) {
            Optional<Image> posterImage = imageService.saveLocal(imgPosterMF, folderName, "poster");
            newProduct.setPoster(posterImage.orElseGet(Image::new).getUrl());
        } else {
            newProduct.setPoster(product.getPoster());
        }


        // Save Preview Image
        try {
            ObjectMapper mapper = new ObjectMapper();
            String[] links = mapper.readValue(imageNames == null ? "[]" : imageNames, String[].class);

            Arrays.stream(links).forEach(
                    link -> {
                        Image image = new Image();
                        image.setUrl(link);
                        image.setProduct(newProduct);
                        imageRepository.save(image);
                    }
            );


        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }


        productRepository.saveAndFlush(newProduct);
        // Save preview images
//        imageService.save(previewImageMF, folderName, "preview-").forEach(
//                image -> {
//                    image.setProduct(newProduct);
//                    imageRepository.save(image);
//                }
//        );

        return newProduct;

    }


    public Optional<Product> restore(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setDeleted(false);
            productRepository.saveAndFlush(product.get());
        }
        return product;
    }

    public MyPage<Product> findAll(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return MyPage.of(page);

    }

    public MyPage<Product> findByCategory(String categoryId, Pageable pageable) {
        Page<Product> page = productRepository.findAllByCategory(categoryId, pageable);
        return MyPage.of(page);
    }

}
