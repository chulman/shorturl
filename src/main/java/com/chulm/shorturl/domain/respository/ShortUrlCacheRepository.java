package com.chulm.shorturl.domain.respository;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.util.Base62Codec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ShortUrlCacheRepository {

    @Value("${spring.redis.default.expire-time-seconds}")
    private long expireTimeSeconds;

    private Logger logger = LoggerFactory.getLogger(ShortUrlCacheRepository.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String NAMESPACE = "short-url";
    private static final String VERSION = "v1";

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;


    public CachedUrl get(String code) throws IOException {
        long id = Base62Codec.decode(code);
        String value = redisConnection.sync().get(getKey(id));

        if(value == null || value.equals("")){
            return null;
        }
        CachedUrl cachedUrl = new CachedUrl();
        cachedUrl.setCode(code);
        cachedUrl.setShortUrl(parse(value));

        return cachedUrl;
    }

    public CachedUrl setCachedShortUrl(ShortUrl shortUrl) {

        String code = Base62Codec.encode((int) shortUrl.getId());

        CachedUrl cachedShortUrl = new CachedUrl();
        cachedShortUrl.setShortUrl(shortUrl);
        cachedShortUrl.setCode(code);

        String value = null;
        try {
            value = OBJECT_MAPPER.writeValueAsString(shortUrl);
            redisConnection.sync().setex(getKey(shortUrl.getId()), expireTimeSeconds, value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cachedShortUrl;
    }

    private ShortUrl parse(String value) throws IOException {
        return OBJECT_MAPPER.readValue(value, ShortUrl.class);
    }

    private String getKey(long id) {
        return String.format("%s-%s(%d)", NAMESPACE, VERSION, id);
    }
}