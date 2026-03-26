package com.shopping_cart.shopping_cart.service.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.shopping_cart.shopping_cart.dto.CategoryDto;
import com.shopping_cart.shopping_cart.exceptions.AlreadyExistsException;
import com.shopping_cart.shopping_cart.model.Category;
import com.shopping_cart.shopping_cart.reponsitory.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categories", key = "#id")
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
            .orElseThrow(()-> new ResourceAccessException("CategoryNotFound"));
    }

    public Category getCategoryByName(String name){
        return categoryRepository.findByName(name);
    }
    @Cacheable(value = "categories", key = "#cursor + '-' + #limit")
    public List<Category> getAllCategories(Long cursor, Long limit){
        return categoryRepository.findAll();
    }

    @CacheEvict(value = "categories", allEntries = true)
    public Category addCategory(CategoryDto category){
        return Optional.of(category)
        .filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(c -> new Category(c.getName()))
        .map(categoryRepository::save)
        .orElseThrow(() -> new AlreadyExistsException("Category existing"));
    }

    @CachePut(value = "categories", key = "#id")
    public Category updateCategory(Category category, Long id){
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(()-> new ResourceAccessException("Category not found"));
    }
    
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategoryById(Long id){
        categoryRepository.findById(id).
        ifPresentOrElse(categoryRepository::delete,
                        () ->{ throw new ResourceAccessException("Category Not Found");});
    }
}
