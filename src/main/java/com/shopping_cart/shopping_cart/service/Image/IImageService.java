package com.shopping_cart.shopping_cart.service.Image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.model.Product;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> savesImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file, Long imageId);
}
