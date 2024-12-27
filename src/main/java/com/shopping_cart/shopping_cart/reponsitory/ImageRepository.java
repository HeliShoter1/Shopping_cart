package com.shopping_cart.shopping_cart.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
    List<Image> findByProductId(Long Id);
}