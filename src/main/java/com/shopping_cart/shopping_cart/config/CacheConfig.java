package com.shopping_cart.shopping_cart.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        // Cấu hình TTL riêng cho từng cache
        Map<String, RedisCacheConfiguration> configs = new HashMap<>();

        configs.put("products", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(5)));

        configs.put("categories", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)));

        configs.put("order-status", RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(30)));

        return RedisCacheManager.builder(factory)
            .withInitialCacheConfigurations(configs)
            .build();
    }
}
