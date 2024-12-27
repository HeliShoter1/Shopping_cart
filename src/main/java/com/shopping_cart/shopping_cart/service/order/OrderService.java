package com.shopping_cart.shopping_cart.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shopping_cart.shopping_cart.enums.OrderStatus;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.shopping_cart.model.Cart;
import com.shopping_cart.shopping_cart.model.Order;
import com.shopping_cart.shopping_cart.model.OrderItem;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.reponsitory.OrderReponsitory;
import com.shopping_cart.shopping_cart.reponsitory.ProductRepository;
import com.shopping_cart.shopping_cart.service.cart.CartService;

import jakarta.transaction.Transactional;

import com.shopping_cart.shopping_cart.dto.OrderDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class OrderService implements IOrderService{
    private final OrderReponsitory orderReponsitory;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public Order placeOrder(Long userId){
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order saveOrder = orderReponsitory.save(order);
        cartService.cleanCart(cart.getId());
        return saveOrder;
    }
    public OrderDto getOrder(Long orderId){
        Order order = orderReponsitory.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        return convertDto(order);
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream().map(cartItem ->{
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order,product,cartItem.getQuantity(),cartItem.getUnitPrice());
        }).toList();
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    public BigDecimal calculateTotalAmount(List<OrderItem> orders){
        return orders.stream()
                        .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public List<OrderDto> getUserOrders(Long userId){
        return orderReponsitory.findByUser_Id(userId).stream().map(this::convertDto).toList();
    }

    private OrderDto convertDto(Order order){
        return modelMapper.map(order,OrderDto.class);
    }

}  
