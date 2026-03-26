package com.shopping_cart.shopping_cart.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping_cart.shopping_cart.model.Cart;

public interface CartReponsitory extends JpaRepository<Cart,Long>{
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId and c.id > :cursor ORDER BY c.id ASC limit :limit")
    Cart findByUserId(Long userId);
}
