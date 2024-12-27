package com.shopping_cart.shopping_cart.sercurity.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.model.User;
import com.shopping_cart.shopping_cart.reponsitory.UserReponsitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService {
    private final UserReponsitory userReponsitory;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userReponsitory.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return ShopUserDetail.buildUserDetails(user);
    }
}