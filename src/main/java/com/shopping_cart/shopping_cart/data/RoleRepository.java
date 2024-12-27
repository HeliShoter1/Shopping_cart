package com.shopping_cart.shopping_cart.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_cart.shopping_cart.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(String role);
    
}  
