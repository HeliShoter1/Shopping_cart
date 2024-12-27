package com.shopping_cart.shopping_cart.reponsitory;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.CartItem;

public interface CartItemReponsitory extends JpaRepository<CartItem,Long>{
    void deleteAllByCartId(Long id);   
    Set<CartItem> findByCart_Id(Long cartId); 
}
