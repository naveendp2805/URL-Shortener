package com.naveen.urlshortener.dto;

import lombok.*;

@Getter
@Builder
public class ShortUrlResponse {

    private Long urlId;
    private String longUrl;
    private String shortUrl;
}
