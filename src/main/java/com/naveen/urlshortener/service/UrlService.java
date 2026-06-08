package com.naveen.urlshortener.service;

import com.naveen.urlshortener.dto.ClickAnalyticsResponse;
import com.naveen.urlshortener.dto.CreateShortUrlRequest;
import com.naveen.urlshortener.dto.ShortUrlResponse;
import com.naveen.urlshortener.dto.UrlMapper;
import com.naveen.urlshortener.exception.ResourceNotFoundException;
import com.naveen.urlshortener.model.Url;
import com.naveen.urlshortener.repository.UrlRepository;
import com.naveen.urlshortener.util.Base62Encoder;
import com.naveen.urlshortener.util.SnowflakeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;
    private final ClickEventProducer clickEventProducer;

    private final SnowflakeGenerator snowflakeGenerator;

    private final RedisService redisService;

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper, RedisService redisService, ClickEventProducer clickEventProducer) {

        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
        this.redisService = redisService;
        this.clickEventProducer = clickEventProducer;

        long MACHINE_ID = Long.parseLong(System.getenv("MACHINE_ID"));
        this.snowflakeGenerator = new SnowflakeGenerator(MACHINE_ID);
    }

    public ShortUrlResponse generateShortUrl(CreateShortUrlRequest request) {

        String longUrl = request.getLongUrl();

        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(longUrl);
        if(existingUrl.isPresent())
            return urlMapper.toDto(existingUrl.get());

        long uniqueId = snowflakeGenerator.generateId();

        String shortCode = Base62Encoder.encode(uniqueId);

        Url url = Url.builder()
                .originalUrl(longUrl)
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .build();

        urlRepository.save(url);

        return urlMapper.toDto(url);
    }

    public String getOriginalUrl(String shortCode) {

        String redisUrl = redisService.get(shortCode);

        if(redisUrl != null) {
            clickEventProducer.publish(shortCode);

            return redisUrl;
        }

        String originalUrl = urlRepository.findOriginalUrlByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Url not found!!"));

        redisService.save(shortCode, originalUrl);

        clickEventProducer.publish(shortCode);

        return originalUrl;
    }

    public ClickAnalyticsResponse getAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found!!"));

        return ClickAnalyticsResponse.builder()
                .shortCode(shortCode)
                .originalUrl(url.getOriginalUrl())
                .clickCount(url.getClickCount())
                .createdAt(url.getCreatedAt())
                .lastAccessedAt(url.getLastAccessedAt())
                .build();
    }
}
