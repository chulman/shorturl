package com.chulm.shorturl.service;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.chulm.shorturl.domain.repository.UrlRepository;
import com.chulm.shorturl.util.Base62Codec;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
public class ShortUrlService {


    @Autowired
    ShortUrlCacheRepository cacheRepository;
    @Autowired
    UrlRepository urlRepository;

    public CachedUrl get(String code) {
        return validate(code);
    }

    @Transactional
    public CachedUrl getOrSave(String url) {
        ShortUrl shortUrl = urlRepository.findByUrl(url);
        if(shortUrl ==null){
            shortUrl = urlRepository.save(new ShortUrl(url, HttpStatus.MOVED_PERMANENTLY.value()));
        }
        log.info("saved linked ShortUrl {}", shortUrl);
        return cacheRepository.setCachedShortUrl(shortUrl);
    }

    public CachedUrl validate(String code) {

        CachedUrl cachedUrl = null;
        try {
            cachedUrl = cacheRepository.get(code);
        } catch (IOException e) {
            log.info("cache data get fail {}", e);
        }

        if (cachedUrl != null) {
            log.info("find cached code = {} url = {}", cachedUrl.getCode(), cachedUrl.getShortUrl());
            return cachedUrl;
        } else {
            //find db
            long id = Base62Codec.decode(code);
            ShortUrl shortUrl = urlRepository.findById(id).orElse(null);
            // set and return cache
            log.info("not found cached code = {} ", id);
            return cacheRepository.setCachedShortUrl(shortUrl);
        }
    }
}