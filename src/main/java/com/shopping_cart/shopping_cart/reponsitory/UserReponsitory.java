package com.shopping_cart.shopping_cart.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.User;

public interface UserReponsitory extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
