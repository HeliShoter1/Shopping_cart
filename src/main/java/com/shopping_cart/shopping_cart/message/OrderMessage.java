package com.shopping_cart.shopping_cart.message;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String status;
}
