package com.chulm.shorturl.service;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.respository.ShortUrlCacheRepository;
import com.chulm.shorturl.domain.respository.UrlRepository;
import com.chulm.shorturl.util.Base62Codec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ShortUrlService {

    private Logger logger = LoggerFactory.getLogger(ShortUrlService.class);

    @Autowired
    ShortUrlCacheRepository cacheRepository;

    /* Interface를 상속받는 여러 객체를 다른 이름으로 지정가능 */
    @Resource(name = "urlRepository")
    UrlRepository urlRepository;

    public CachedUrl get(String code) {
        return validate(code);
    }

    public CachedUrl saveDB(String url) {
        ShortUrl shortUrl = urlRepository.save(new ShortUrl(url, HttpStatus.MOVED_PERMANENTLY.value()));
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