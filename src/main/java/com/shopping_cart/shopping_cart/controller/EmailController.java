package com.shopping_cart.shopping_cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_cart.shopping_cart.service.email.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/email")
@RequiredArgsConstructor
public class EmailController {

        private final EmailService emailService;

        @PostMapping("/simple")
        public ResponseEntity<String> sendSimple(@RequestParam String to) {
            emailService.sendSimpleEmail(
                to,
                "Xin chào!",
                "Đây là email text thuần từ Spring Boot."
            );
            return ResponseEntity.ok("Gửi thành công!");
        } {
        
    }
}
