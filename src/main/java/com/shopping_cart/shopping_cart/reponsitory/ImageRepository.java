package com.shopping_cart.shopping_cart.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping_cart.shopping_cart.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
    @Query("SELECT i FROM Image i WHERE i.product.id = :Id")
    List<Image> findByProductId(Long Id);
}   