package com.shopping_cart.shopping_cart.dto;

import java.math.BigDecimal;
import java.util.Optional;

import com.shopping_cart.shopping_cart.model.Product;

import lombok.Data;

@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}