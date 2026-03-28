package com.shopping_cart.shopping_cart.reponsitory;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.shopping_cart.shopping_cart.model.CartItem;

public interface CartItemReponsitory extends JpaRepository<CartItem,Long>{
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :id")
    void deleteAllByCartId(Long id);   

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId")
    Set<CartItem> findByCart_Id(Long cartId); 
}
