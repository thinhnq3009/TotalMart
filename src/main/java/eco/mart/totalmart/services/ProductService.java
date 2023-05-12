package eco.mart.totalmart.services;

import eco.mart.totalmart.dto.ProductDto;
import eco.mart.totalmart.entities.Image;
import eco.mart.totalmart.entities.Product;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.repositories.ImageRepository;
import eco.mart.totalmart.repositories.ProductRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
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

    public Product upsert(Product product, MultipartFile imgPosterMF, MultipartFile[] previewImageMF) {



        // Save product to get product's id
        Product newProduct = productRepository.saveAndFlush(product);



        String folderName = "/image/products/product-%s".formatted(newProduct.getId() + "");

        // Save poster image
        Optional<Image> posterImage = imageService.saveLocal(imgPosterMF, folderName, "poster");
        newProduct.setPoster(posterImage.orElseGet(Image::new).getUrl());

        // Save preview images
        imageService.save(previewImageMF, folderName, "preview-").forEach(
                image -> {
                    image.setProduct(newProduct);
                    imageRepository.save(image);
                }
        );

        return newProduct;

    }


}
