package com.shopping_cart.shopping_cart.consumer;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.shopping_cart.shopping_cart.config.RabbitMQConfig;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.reponsitory.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageConsumer {
    
    private ImageRepository imageRepository;

    @RabbitListener(queues =  RabbitMQConfig.EMAIL_QUEUE)
    public void handleImage(List<Image>  images){
        log.info("Handling {} images", images.size());
        String buildDownloadUrl = "/api/v1/images/image/download/";

        images.forEach(image -> {
            try{
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);
                log.info("Saved image: {}", savedImage.getId());
            } catch (Exception e) {
                log.error("Failed to save image: {}, error: {}", image.getFileName(), e.getMessage());
            }
        });
    }
}
