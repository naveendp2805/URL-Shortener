package com.naveen.urlshortener.service;

import com.naveen.urlshortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalyticsService {

    private final UrlRepository urlRepository;

    public void incrementClickCount(String shortCode) {

        urlRepository.incrementClickAnalytics(shortCode);
    }
}