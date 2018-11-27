package com.chulm.shorturl.domain.respository;

import com.chulm.shorturl.domain.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("urlRepository")
public interface UrlRepository extends JpaRepository<ShortUrl,Long> {
}

