package com.shopping_cart.shopping_cart.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  com.shopping_cart.shopping_cart.model.Category findByName(String name);

  boolean existsByName(String name);
}
