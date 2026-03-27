package com.shopping_cart.shopping_cart.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.shopping_cart.shopping_cart.config.RabbitMQConfig;
import com.shopping_cart.shopping_cart.message.EmailMessage;
import com.shopping_cart.shopping_cart.service.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {

    private final EmailService emailService; // class chứa sendSimpleEmail của bạn

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmail(EmailMessage message) {
        log.info("Sending email to: {}", message.getTo());
        try {
            emailService.sendSimpleEmail(
                message.getTo(),
                message.getSubject(),
                message.getBody()
            );
            log.info("Email sent successfully to: {}", message.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", message.getTo(), e.getMessage());
            throw e; 
        }
    }
}
