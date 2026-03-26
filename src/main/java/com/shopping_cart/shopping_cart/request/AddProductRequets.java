package com.shopping_cart.shopping_cart.request;

import java.math.BigDecimal;

import com.shopping_cart.shopping_cart.model.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@Valid
public class AddProductRequets {
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int inventory;
    private Category category;    
}
