package com.shopping_cart.shopping_cart.service.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.dto.CartItemDto;
import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.CartItem;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.reponsitory.CartItemReponsitory;
import com.shopping_cart.shopping_cart.reponsitory.CartReponsitory;
import com.shopping_cart.shopping_cart.reponsitory.ProductRepository;
import com.shopping_cart.shopping_cart.service.Product.IProductService;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService implements ICartItemService{

    private final CartItemReponsitory cartItemReponsitory;

    
    private final ICartService cartService; 
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    private final CartReponsitory cartReponsitory;
    private final IProductService productService;

    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId); 
        System.out.println(cart.toString());
        Product product = productService.getProductById(productId);
    
        CartItem cartItem = cart.getCartItems()
                                .stream()
                                .filter(item -> item.getProduct().getId().equals(productId))
                                .findFirst()
                                .orElse(new CartItem());
        if (cartItem.getId() == null) { 
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemReponsitory.save(cartItem);
        cartReponsitory.save(cart);
    }
    
    public void removeItemFromCart(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartReponsitory.save(cart);
    }
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getCartItems()
                .stream().map(CartItem ::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartReponsitory.save(cart);
    }

    public CartItem getCartItem(Long cartItem,Long productId){
        Cart cart = cartService.getCart(cartItem);
        return  cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

   
}
