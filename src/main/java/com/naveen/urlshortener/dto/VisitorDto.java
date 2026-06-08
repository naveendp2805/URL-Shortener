package com.naveen.urlshortener.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitorDto {

    private String ipAddress;

    private String userAgent;

    private LocalDateTime clickedAt;
}
