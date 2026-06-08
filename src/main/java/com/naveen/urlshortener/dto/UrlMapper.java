package com.naveen.urlshortener.dto;

import com.naveen.urlshortener.model.Url;
import org.springframework.stereotype.Component;

@Component
public class UrlMapper {

    public ShortUrlResponse toDto(Url url) {

        return ShortUrlResponse.builder()
                .urlId(url.getId())
                .longUrl(url.getOriginalUrl())
                .shortUrl(url.getShortCode())
                .build();
    }
}
