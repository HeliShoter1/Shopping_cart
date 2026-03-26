package com.shopping_cart.shopping_cart.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shopping_cart.shopping_cart.dto.UserDto;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.User;

public interface UserReponsitory extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    @Query("""
    SELECT new com.shopping_cart.shopping_cart.dto.UserDto(
        u.id, u.firstName, u.lastName, u.email
    )
    FROM User u
    WHERE u.id = :id
    """)
    Optional<UserDto> findUserById(@Param("id") Long id);
    

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Modifying      
    @Transactional(propagation = Propagation.REQUIRED)
    @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);
}
