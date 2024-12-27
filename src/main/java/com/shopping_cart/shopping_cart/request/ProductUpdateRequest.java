package com.shopping_cart.shopping_cart.request;

import lombok.Data;

import java.math.BigDecimal;

import com.shopping_cart.shopping_cart.model.Category;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
