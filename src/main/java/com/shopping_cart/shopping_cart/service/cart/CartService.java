package com.shopping_cart.shopping_cart.service.cart;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.dto.CartDto;
import com.shopping_cart.shopping_cart.dto.CartItemDto;
import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.CartItem;
import com.shopping_cart.shopping_cart.model.User;
import com.shopping_cart.shopping_cart.reponsitory.CartItemReponsitory;
import com.shopping_cart.shopping_cart.reponsitory.CartReponsitory;
import com.shopping_cart.shopping_cart.reponsitory.ProductRepository;
import com.shopping_cart.shopping_cart.service.Product.IProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService implements ICartService{

    private final CartReponsitory cartReponsitory;
    private final CartItemReponsitory cartItemReponsitory;
    private final IProductService productService;

    private final ModelMapper modelMapper;

    public Cart getCart(Long id) {
        Cart cart = cartReponsitory.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartReponsitory.save(cart);
    }

    public void cleanCart(Long id){
        Cart cart = getCart(id);
        cartItemReponsitory.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartReponsitory.deleteById(id);
    }

    public BigDecimal getTotalPrice(Long id){
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User userId) {
        Cart newCart = new Cart();
        newCart.setUser(userId);
        return cartReponsitory.save(newCart);
    }

    public CartItemDto convertCartItemDto(CartItem cart){
        ProductDto productDto = productService.convertDto(cart.getProduct());
        CartItemDto cartItemDto = modelMapper.map(cart, CartItemDto.class);
        cartItemDto.setProduct(productDto);
        return cartItemDto;
    }

    @Override
    public CartDto converCartDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        Set<CartItemDto> cartItemDtos = cart.getCartItems()
                                    .stream()
                                    .map(cartItem -> convertCartItemDto(cartItem))
                                    .collect(Collectors.toSet());
        cartDto.setItems(cartItemDtos);
        return cartDto;
    }

    public Cart getCartByUserId(Long userId){
        return cartReponsitory.findByUserId(userId);
    }
}