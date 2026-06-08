package com.naveen.urlshortener.repository;

import com.naveen.urlshortener.model.ClickAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickAnalyticsRepository extends JpaRepository<ClickAnalytics, Long> {

    long countDistinctIpAddressByShortCode(String shortCode);

    List<ClickAnalytics> findTop10ByShortCodeOrderByClickedAtDesc(String shortCode);
}
