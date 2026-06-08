package com.naveen.urlshortener.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortUrlResponse {

    private Long urlId;
    private String longUrl;
    private String shortUrl;
}
