package com.shopping_cart.shopping_cart.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Order;

public interface OrderReponsitory extends JpaRepository<Order, Long>{
    List<Order> findByUser_Id(Long userId);
}  
