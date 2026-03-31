package com.shopping_cart.shopping_cart.consumer;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import com.shopping_cart.shopping_cart.config.RabbitMQConfig;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.reponsitory.ImageRepository;
import com.shopping_cart.shopping_cart.reponsitory.ProductRepository;
import com.shopping_cart.shopping_cart.service.Image.ImageService;
import com.shopping_cart.shopping_cart.service.Product.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageConsumer {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;
    
    private final ImageService imageService;
    private final ProductService productService;

    @RabbitListener(queues = RabbitMQConfig.IMAGE_QUEUE)
    public void handleImages(List<Image> images) {
        log.info("Handling {} images", images.size());
        String buildDownloadUrl = "/api/v1/images/image/download/";

        images.forEach(image -> {
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Product product = productService.getProductById(image.getProductId());
                image.setProduct(product);

                String fileName = System.currentTimeMillis() + "_" + image.getFileName();
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, image.getImageData());

                image.setFilePath(filePath.toString());
                Image savedImage = imageService.saveImage(image);
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageService.saveImage(savedImage);

                log.info("Saved image: {}", savedImage.getId());
            } catch (Exception e) {
                log.error("Failed to save image: {}, error: {}", image.getFileName(), e.getMessage());
            }
        });
    }
}
