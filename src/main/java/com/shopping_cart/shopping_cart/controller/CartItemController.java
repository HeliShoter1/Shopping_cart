package com.shopping_cart.shopping_cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.User;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.IUserService;
import com.shopping_cart.shopping_cart.service.cart.ICartItemService;
import com.shopping_cart.shopping_cart.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                    @RequestParam Integer quantity
                                                    ){
        try {
            User user = userService.getAuthenticateUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                cart= cartService.initializeNewCart(user);
              }
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok( new ApiResponse("Add success", null));
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,@PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{userId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long userId,
                                                            @PathVariable Long itemId,
                                                            @RequestParam Integer quantity){
        try {
            Long cartId = userService.getAuthenticateUser().getId();
            cartItemService.updateItemQuantity(cartId, itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    
}
