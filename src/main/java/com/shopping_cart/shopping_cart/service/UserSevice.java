package com.shopping_cart.shopping_cart.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.dto.UserDto;
import com.shopping_cart.shopping_cart.exceptions.AlreadyExistsException;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.User;
import com.shopping_cart.shopping_cart.reponsitory.UserReponsitory;
import com.shopping_cart.shopping_cart.request.CreateUserRequest;
import com.shopping_cart.shopping_cart.request.UserUpdateRequest;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSevice implements IUserService{
    private final UserReponsitory userReponsitory;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    
    public User getUseById(Long userId){
        return userReponsitory.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
    public User createUser(CreateUserRequest request){
        return Optional.of(request)
                        .filter(user -> !userReponsitory.existsByEmail(request.getEmail()))
                        .map(req->{
                            User user = new User();
                            user.setEmail(request.getEmail());
                            user.setPassword(passwordEncoder.encode(request.getPassword()));
                            user.setFirstName(request.getFirstName());
                            user.setLastName(request.getLastName());
                            return userReponsitory.save(user);
                        }).orElseThrow(() -> new AlreadyExistsException("Email already exists"));
    }
    public User updateUser(UserUpdateRequest request, Long userId){
        return userReponsitory.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userReponsitory.save(existingUser);
        }).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
    public void deleteUser(Long userId){
        userReponsitory.findById(userId).ifPresentOrElse(userReponsitory::delete, ()->{
            throw new ResourceNotFoundException("User not found");
        });
    }
    public UserDto convertDto(User user){
        return modelMapper.map(user, UserDto.class);  
    }
    @Override
    public User getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userReponsitory.findByEmail(email);
    }
}  
