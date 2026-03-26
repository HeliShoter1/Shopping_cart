package com.shopping_cart.shopping_cart.dto;

import java.util.List;

import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.Order;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    // private List<OrderDto> orders;    
    // private Cart cart;

    public UserDto() {} 

    public UserDto(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}  
