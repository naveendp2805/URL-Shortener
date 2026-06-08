package com.naveen.urlshortener.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VisitorAnalyticsResponse {

    private String shortCode;

    private Long totalClicks;

    private Long uniqueVisitors;

    private List<VisitorDto> recentVisitors;
}
