package com.shopping_cart.shopping_cart.service.cart;

import java.math.BigDecimal;

import com.shopping_cart.shopping_cart.dto.CartDto;
import com.shopping_cart.shopping_cart.dto.CartItemDto;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.CartItem;
import com.shopping_cart.shopping_cart.model.User;

public interface ICartService {

    Cart getCart(Long id);
    void cleanCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User userId);
    CartItemDto convertCartItemDto(CartItem cart);
    CartDto converCartDto(Cart cart);
    Cart getCartByUserId(Long userId);
}
