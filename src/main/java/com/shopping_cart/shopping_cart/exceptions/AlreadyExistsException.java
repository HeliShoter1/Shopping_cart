package com.shopping_cart.shopping_cart.exceptions; 

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}