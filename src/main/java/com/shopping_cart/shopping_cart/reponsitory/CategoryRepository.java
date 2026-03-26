package com.shopping_cart.shopping_cart.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping_cart.shopping_cart.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT c FROM Category c WHERE c.name = :name")
  Category findByName(String name);

  @Query("""
    SELECT CASE 
          WHEN EXISTS (
              SELECT 1 FROM Category c WHERE c.name = :name
          ) 
          THEN TRUE ELSE FALSE 
    END
  """)
  boolean existsByName(String name);
}
