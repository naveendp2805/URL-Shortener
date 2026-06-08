package com.naveen.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String shortCode, String originalUrl) {

        redisTemplate.opsForValue().set("url:" + shortCode, originalUrl, Duration.ofDays(7));
    }

    public String get(String shortCode) {
        return redisTemplate.opsForValue().get("url:" + shortCode);
    }
}
