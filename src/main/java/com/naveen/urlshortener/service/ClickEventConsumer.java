package com.naveen.urlshortener.service;

import com.naveen.urlshortener.event.ClickEvent;
import com.naveen.urlshortener.event.KafkaTopics;
import com.naveen.urlshortener.model.ClickAnalytics;
import com.naveen.urlshortener.repository.ClickAnalyticsRepository;
import com.naveen.urlshortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickEventConsumer {

    private final AnalyticsService analyticsService;
    private final ClickAnalyticsRepository clickAnalyticsRepository;

    @KafkaListener(topics = KafkaTopics.CLICK_EVENTS, groupId = "click-analytics-group")
    public void consume(ClickEvent event) {

        log.info("processing click event for {}", event.getShortCode());

        analyticsService.incrementClickCount(event.getShortCode());

        ClickAnalytics clickAnalytics = ClickAnalytics.builder()
                .shortCode(event.getShortCode())
                .ipAddress(event.getIpAddress())
                .userAgent(event.getUserAgent())
                .clickedAt(event.getClickedAt())
                .build();

        clickAnalyticsRepository.save(clickAnalytics);
    }

}
