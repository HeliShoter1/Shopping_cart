package com.shopping_cart.shopping_cart.service;

import com.shopping_cart.shopping_cart.dto.UserDto;
import com.shopping_cart.shopping_cart.model.User;
import com.shopping_cart.shopping_cart.request.CreateUserRequest;
import com.shopping_cart.shopping_cart.request.UserUpdateRequest;

public interface IUserService {

    User getUseById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);
    UserDto convertDto(User user);
    User getAuthenticateUser();
}  
