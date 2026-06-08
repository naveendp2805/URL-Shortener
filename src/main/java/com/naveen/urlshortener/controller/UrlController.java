package com.naveen.urlshortener.controller;

import com.naveen.urlshortener.dto.ClickAnalyticsResponse;
import com.naveen.urlshortener.dto.CreateShortUrlRequest;
import com.naveen.urlshortener.dto.ShortUrlResponse;
import com.naveen.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.Jar;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/url")
    public ResponseEntity<ShortUrlResponse> generateShortUrl(@RequestBody CreateShortUrlRequest request) {
        return ResponseEntity.status(200).body(urlService.generateShortUrl(request));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {

        String originalUrl = urlService.getOriginalUrl(shortCode);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setLocation(URI.create(originalUrl));

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<ClickAnalyticsResponse> getAnalytics(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getAnalytics(shortCode));
    }
}
