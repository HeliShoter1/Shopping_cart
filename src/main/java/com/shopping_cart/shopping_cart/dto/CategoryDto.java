package com.shopping_cart.shopping_cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CategoryDto {
    @JsonProperty("name")
    private String name;
}
