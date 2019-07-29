package com.chulm.shorturl.config;

import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${spring.redis.default.expire-time-seconds}")
    long expireTime;

    @Bean
    public ShortUrlCacheRepository cacheRepository(StatefulRedisConnection<String, String> redisConnection){
        return new ShortUrlCacheRepository(redisConnection, expireTime);
    }

}
