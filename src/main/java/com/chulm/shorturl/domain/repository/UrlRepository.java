package com.chulm.shorturl.domain.repository;

import com.chulm.shorturl.domain.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UrlRepository extends JpaRepository<ShortUrl,Object> {
    @Query("select u from SHORT_URL u where u.link = ?1")
    ShortUrl findByUrl(String link);
}

