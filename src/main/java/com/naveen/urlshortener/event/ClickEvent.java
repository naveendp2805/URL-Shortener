package com.naveen.urlshortener.event;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ClickEvent {

    private String shortCode;
    private LocalDateTime clickedAt;
}
