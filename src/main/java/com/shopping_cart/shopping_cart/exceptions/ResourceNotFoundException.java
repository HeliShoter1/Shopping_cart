package com.shopping_cart.shopping_cart.exceptions; 

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);

    }
}