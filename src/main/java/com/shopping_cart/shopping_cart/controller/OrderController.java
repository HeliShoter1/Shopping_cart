package com.shopping_cart.shopping_cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_cart.shopping_cart.dto.OrderDto;
import com.shopping_cart.shopping_cart.message.EmailMessage;
import com.shopping_cart.shopping_cart.message.OrderMessage;
import com.shopping_cart.shopping_cart.model.Order;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.sercurity.user.ShopUserDetail;
import com.shopping_cart.shopping_cart.service.MessageProducer;
import com.shopping_cart.shopping_cart.service.email.EmailService;
import com.shopping_cart.shopping_cart.service.order.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
public ResponseEntity<ApiResponse> createOrder() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = auth.getPrincipal();
    if (!(principal instanceof ShopUserDetail)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse("Unauthorized", null));
    }

    ShopUserDetail currentUser = (ShopUserDetail) principal;
    try {
        orderService.placeOrder(currentUser.getId());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResponse("Đơn hàng đang được xử lý", null));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error", e.getMessage()));
    }
}   

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok( new ApiResponse("Success", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@RequestParam Long cursor, @RequestParam Long limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    
        Object principal = auth.getPrincipal();
        if (!(principal instanceof ShopUserDetail)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Unauthorized", null));
        }
    
        ShopUserDetail currentUser = (ShopUserDetail) principal;
        try {
            List<OrderDto> order = orderService.getUserOrders(currentUser.getId(),cursor, limit);
            return ResponseEntity.ok( new ApiResponse("Success", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( new ApiResponse("Error", e.getMessage()));
        }
    }
    
}  
