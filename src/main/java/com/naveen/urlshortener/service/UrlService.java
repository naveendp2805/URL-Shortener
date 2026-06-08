package com.naveen.urlshortener.service;

import com.naveen.urlshortener.dto.*;
import com.naveen.urlshortener.exception.ResourceNotFoundException;
import com.naveen.urlshortener.model.ClickAnalytics;
import com.naveen.urlshortener.model.Url;
import com.naveen.urlshortener.repository.ClickAnalyticsRepository;
import com.naveen.urlshortener.repository.UrlRepository;
import com.naveen.urlshortener.util.Base62Encoder;
import com.naveen.urlshortener.util.SnowflakeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UrlService {

    private final UrlRepository urlRepository;
    private final ClickAnalyticsRepository clickAnalyticsRepository;

    private final UrlMapper urlMapper;
    private final ClickEventProducer clickEventProducer;

    private final SnowflakeGenerator snowflakeGenerator;

    private final RedisService redisService;

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper, RedisService redisService, ClickEventProducer clickEventProducer, ClickAnalyticsRepository clickAnalyticsRepository) {

        this.urlRepository = urlRepository;
        this.clickAnalyticsRepository = clickAnalyticsRepository;
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

    public String getOriginalUrl(String shortCode, HttpServletRequest request) {

        String redisUrl = redisService.get(shortCode);

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        if(redisUrl != null) {
            clickEventProducer.publish(shortCode, ipAddress, userAgent);

            return redisUrl;
        }

        String originalUrl = urlRepository.findOriginalUrlByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Url not found!!"));

        redisService.save(shortCode, originalUrl);

        clickEventProducer.publish(shortCode, ipAddress, userAgent);

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

    public VisitorAnalyticsResponse getVisitorAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found!!"));

        long uniqueVisitors = clickAnalyticsRepository.countDistinctIpAddressByShortCode(shortCode);

        List<ClickAnalytics> clicks = clickAnalyticsRepository.findTop10ByShortCodeOrderByClickedAtDesc(shortCode);

        List<VisitorDto> recentVisitors = clicks.stream()
                .map(click -> VisitorDto.builder()
                        .ipAddress(click.getIpAddress())
                        .userAgent(click.getUserAgent())
                        .clickedAt(click.getClickedAt())
                        .build()
                )
                .toList();

        return VisitorAnalyticsResponse.builder()
                .shortCode(shortCode)
                .totalClicks(url.getClickCount())
                .uniqueVisitors(uniqueVisitors)
                .recentVisitors(recentVisitors)
                .build();
    }
}
