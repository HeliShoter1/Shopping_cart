package com.shopping_cart.shopping_cart.service.cart;

import java.util.Set;

import com.shopping_cart.shopping_cart.dto.CartItemDto;
import com.shopping_cart.shopping_cart.model.CartItem;

public interface ICartItemService {

    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartItem,Long productId);

    
}