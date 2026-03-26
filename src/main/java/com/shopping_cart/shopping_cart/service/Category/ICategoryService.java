package com.shopping_cart.shopping_cart.service.Category;

import java.util.List;

import com.shopping_cart.shopping_cart.dto.CategoryDto;
import com.shopping_cart.shopping_cart.model.Category;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories(Long cursor, Long limit);
    Category addCategory(CategoryDto category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}