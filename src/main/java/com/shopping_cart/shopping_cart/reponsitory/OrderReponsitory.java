package com.shopping_cart.shopping_cart.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopping_cart.shopping_cart.model.Order;

public interface OrderReponsitory extends JpaRepository<Order, Long>{
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId and o.id > :cursor ORDER by id ASC limit :limit")
    List<Order> findByUser_Id(Long userId, Long cursor, Long limit);
}  
