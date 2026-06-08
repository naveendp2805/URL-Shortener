package com.naveen.urlshortener.service;

import com.naveen.urlshortener.event.ClickEvent;
import com.naveen.urlshortener.event.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickEventProducer {

    private final KafkaTemplate<String, ClickEvent> kafkaTemplate;

    public void publish(String shortCode, String ipAddress, String userAgent) {

        log.info("Publishing click event for {}", shortCode);

        ClickEvent event = ClickEvent.builder()
                .shortCode(shortCode)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .clickedAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(KafkaTopics.CLICK_EVENTS, shortCode, event);
    }
}
