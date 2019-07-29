package com.chulm.shorturl.service;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.chulm.shorturl.domain.repository.ShortUrlRepository;
import com.chulm.shorturl.util.Base62Codec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.Objects;

@Slf4j
@Service
public class ShortUrlService {

    @Autowired
    ShortUrlCacheRepository cacheRepository;
    @Autowired
    ShortUrlRepository urlRepository;

    public Observable<CachedUrl> get(String code) {
        return cacheRepository.get(code)
                .filter(Objects::nonNull)
                .switchIfEmpty(
                        urlRepository.findById(Base62Codec.decode(code))
                                     .doOnNext(shortUrl -> cacheRepository.setCachedShortUrl(shortUrl).subscribe())
                ).map(shortUrl ->  CachedUrl.builder().code(Base62Codec.encode((int)shortUrl.getId()))
                                                     .shortUrl(shortUrl)
                                                     .build());
    }

    public Observable<CachedUrl> save(String url){
      return urlRepository.save(ShortUrl.builder().status(301).link(url).build())
              .switchMap(integer -> {
                  if (integer == 1) {
                      return urlRepository.findByUrl(url);
                  }
                  return null;
              })
              .filter(Objects::nonNull)
              .map(shortUrl -> CachedUrl.builder().code(Base62Codec.encode((int) shortUrl.getId()))
                                                  .shortUrl(shortUrl).build());
    }



}