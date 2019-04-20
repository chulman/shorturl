package com.chulm.shorturl.service;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.chulm.shorturl.domain.repository.UrlRepository;
import com.chulm.shorturl.util.Base62Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShortUrlService {

    private Logger logger = LoggerFactory.getLogger(ShortUrlService.class);

    @Autowired
    ShortUrlCacheRepository cacheRepository;
    @Autowired
    UrlRepository urlRepository;

    public CachedUrl get(String code) {
        return validate(code);
    }

    public CachedUrl getAndSave(String url) {
        ShortUrl shortUrl = urlRepository.findByUrl(url);
        if(shortUrl ==null){
            shortUrl = urlRepository.save(new ShortUrl(url, HttpStatus.MOVED_PERMANENTLY.value()));
        }
        logger.info("saved linked ShortUrl {}", shortUrl.toString());
        return cacheRepository.setCachedShortUrl(shortUrl);
    }

    public CachedUrl validate(String code) {

        CachedUrl cachedUrl = null;
        try {
            cachedUrl = cacheRepository.get(code);
        } catch (IOException e) {
            logger.info("cache data get fail {}", e);
        }

        if (cachedUrl != null) {
            logger.info("find cached code = {} url = {}", cachedUrl.getCode(), cachedUrl.getShortUrl());
            return cachedUrl;
        } else {
            //find db
            long id = Base62Codec.decode(code);
            ShortUrl shortUrl = urlRepository.findById(id).orElse(null);
            // set and return cache

            return cacheRepository.setCachedShortUrl(shortUrl);
        }
    }
}