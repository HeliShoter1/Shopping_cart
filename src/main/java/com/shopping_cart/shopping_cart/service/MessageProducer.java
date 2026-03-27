package com.shopping_cart.shopping_cart.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.shopping_cart.shopping_cart.config.RabbitMQConfig;
import com.shopping_cart.shopping_cart.message.EmailMessage;
import com.shopping_cart.shopping_cart.message.OrderMessage;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {
    private final AmqpTemplate amqpTemplate;

    public void sendOrderCreated(OrderMessage message) {
        amqpTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ORDER_ROUTING_KEY,
            message
        );
        log.info("Sent order message: {}", message.getOrderId());
    }

    public void sendEmail(EmailMessage message) {
        amqpTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.EMAIL_ROUTING_KEY,
            message
        );
    }
}
