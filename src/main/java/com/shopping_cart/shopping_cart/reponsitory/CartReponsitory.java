package com.shopping_cart.shopping_cart.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Cart;

public interface CartReponsitory extends JpaRepository<Cart,Long>{
    Cart findByUserId(Long userId);
}
