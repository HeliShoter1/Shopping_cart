package com.shopping_cart.shopping_cart.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping_cart.shopping_cart.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    List<Product> findByCategoryName(String category);
    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> findByBrand(String brand);
    @Query("SELECT p FROM Product p WHERE p.category.name = :category AND p.brand = :brand")
    List<Product> findByCategoryNameAndBrand(String category, String brand);
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByName(String name);
    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.name = :name")
    List<Product> findByBrandAndName(String brand, String name);
    @Query("SELECT COUNT(p) FROM Product p WHERE p.brand = :brand AND p.name = :name")
    Long countByBrandAndName(String brand, String name);
    @Query("SELECT p FROM Product p where id > :cursor order by id limit :limit")
    List<Product> findAll(Long cursor, Long limit);
    @Query("""
    SELECT CASE 
          WHEN EXISTS (
              SELECT 1 FROM Product p WHERE p.name = :name AND p.brand = :brand
          ) 
          THEN TRUE ELSE FALSE 
    END
  """)
    boolean existsByNameAndBrand(String name, String brand);
}