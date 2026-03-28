package com.shopping_cart.shopping_cart.config;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Component
public class DotenvConfig {
    
    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // không báo lỗi nếu không có file .env
                .load();
        
        dotenv.entries().forEach(entry -> 
            System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}
