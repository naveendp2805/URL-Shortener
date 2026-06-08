package com.naveen.urlshortener.repository;

import com.naveen.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByOriginalUrl(String originalUrl);

    @Query("""
       SELECT s.originalUrl
       FROM Url s
       WHERE s.shortCode = :shortCode
       """)
    Optional<Url> findOriginalUrlByShortCode(@Param("shortCode") String shortCode);

    @Modifying
    @Query("""
            UPDATE Url s
            SET s.clickCount = s.clickCount + 1,
                s.lastAccessedAt = CURRENT_TIMESTAMP 
            WHERE s.shortCode = :shortCode
            """)
    int incrementClickAnalytics(@Param("shortCode") String shortCode);
}
