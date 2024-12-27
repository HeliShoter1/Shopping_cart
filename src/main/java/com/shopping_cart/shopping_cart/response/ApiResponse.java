package com.shopping_cart.shopping_cart.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
@Setter
@Getter
public class ApiResponse {
    private String message;
    private Object data;    
}
