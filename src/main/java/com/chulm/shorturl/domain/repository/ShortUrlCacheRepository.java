package com.chulm.shorturl.domain.repository;

import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.util.Base62Codec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class ShortUrlCacheRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String NAMESPACE = "short-url";
    private static final String VERSION = "v1";

    private StatefulRedisConnection<String, String> redisConnection;
    private final long expireTimeSeconds;


    public Observable<ShortUrl> get(String code) {
        return redisConnection.reactive()
                              .get(getKey(Base62Codec.decode(code)))
                              .doOnNext(s -> log.info("extract cache = {}", s))
                              .filter(Objects::nonNull)
                              .map(this::toShortUrl);
    }

    public Observable<String> setCachedShortUrl(ShortUrl shortUrl) {
        return redisConnection.reactive()
                              .setex(getKey(shortUrl.getId()),
                                            expireTimeSeconds,
                                            shortUrlToString(shortUrl))
                              .doOnNext(s -> log.info("cache hit = {}", s));
    }

    private String shortUrlToString(ShortUrl shortUrl){
        try {
            return OBJECT_MAPPER.writeValueAsString(shortUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ShortUrl toShortUrl(String value){
        try {
            return OBJECT_MAPPER.readValue(value, ShortUrl.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getKey(long id) {
        return String.format("%s-%s(%d)", NAMESPACE, VERSION, id);
    }
}