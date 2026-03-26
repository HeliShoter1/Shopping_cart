package com.shopping_cart.shopping_cart.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.shopping_cart.shopping_cart.annotation.RateLimit;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object limit(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)
            RequestContextHolder.getRequestAttributes()).getRequest();

        String key = request.getRemoteAddr() + ":" + pjp.getSignature();
        Bucket bucket = buckets.computeIfAbsent(key, k ->
            Bucket.builder()
                .addLimit(Bandwidth.classic(
                    rateLimit.requests(),
                    Refill.greedy(rateLimit.requests(),
                                  Duration.ofSeconds(rateLimit.seconds()))
                ))
                .build()
        );

        if (bucket.tryConsume(1)) {
            return pjp.proceed();
        }

        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                                          "Rate limit exceeded");
    }
}
