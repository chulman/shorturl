package com.chulm.shorturl.config;

import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Profile("local")
@Configuration
public class JedisConfig {

    private @Value("${spring.redis.host}")
    String redisHost;
    private @Value("${spring.redis.port}")
    int redisPort;
    private @Value("${spring.redis.password}")
    String password;


    private @Value("${spring.jedis.pool.max-idle}")
    int maxIdle;
    private @Value("${spring.jedis.pool.min-idle}")
    int minIdle;
    private @Value("${spring.jedis.pool.max-wait}")
    long maxWait;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        return jedisPoolConfig;
    }

    /*
     * 사용되지 않는 변수 및 개인 메소드에 대한 제한
     * SuppressWarnings = 콘솔 화면에서 특정 경고를 표시하도록 설정
     */
    @SuppressWarnings("deprecation")
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
//      JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setUsePool(false);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setEnableDefaultSerializer(false);
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(jedisConnectionFactory());
        stringRedisTemplate.setEnableDefaultSerializer(false);
        stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }

    /*
     * 컨테이너에서 객체를 제거하기 전에 해야할 작업을 수행하기 위해 사용한다.
     */
    @PreDestroy
    public void cleanRedis() {
        jedisConnectionFactory().getConnection().flushDb();
    }
}