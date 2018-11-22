package com.chulm.test.testcase;

import com.chulm.shorturl.config.JedisConfig;
import com.chulm.shorturl.config.LettuceConfig;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LettuceConfig.class, JedisConfig.class})
public class RedisLibraryTimeCheck_test {

    @Autowired
    StatefulRedisConnection<String, String> lettuceConnection;
    @Autowired
    private RedisTemplate<String, Object> jedisTemplate;


    private int count = 10000;

    @Test
    public void JedisSave() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String key = "jedis" + i;
            String value = String.valueOf(i);

            jedisTemplate.opsForValue().append(key,value);
        }
        System.err.println("jedis end :"  + (System.currentTimeMillis() - start));
    }

    @Test
    public void LettuceSave() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String key = "lettuce" + i;
            String value = String.valueOf(i);
            lettuceConnection.sync().append(key,value);
        }
        System.err.println("lettuce sync end :"  + (System.currentTimeMillis() - start));
    }

    @Test
    public void LettuceAsyncSave() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String key = "lettuceAsync" + i;
            String value = String.valueOf(i);
            lettuceConnection.async().append(key,value);
        }
        System.err.println("lettuce async end :"  + (System.currentTimeMillis() - start));
    }

}
