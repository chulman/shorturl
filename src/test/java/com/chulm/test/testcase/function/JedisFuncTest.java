package com.chulm.test.testcase.function;

import com.chulm.shorturl.config.JedisConfig;
import com.chulm.shorturl.config.LettuceConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;


//http://kingbbode.tistory.com/25 [개발노트 - kingbbode]
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JedisConfig.class})
public class JedisFuncTest {

    @Resource
    private ValueOperations<String, String> valueOperations;
    @Resource
    private ListOperations<String, String> listOperations;
    @Resource
    private HashOperations<String, String, String> hashOperations;
    @Resource
    private SetOperations<String, String> setOperations;
    @Resource
    private ZSetOperations<String, String> zSetOperations;


    @Before
    public void input() {

        /**
         * String
         */
        valueOperations.set("key", "value");


        /**
         * list : 왼쪽 혹은 오른쪽 부터 자료구조 찾을 수 있다.
         */
        listOperations.rightPush("test", "1");
        listOperations.rightPush("test", "2");
        listOperations.rightPush("test", "3");
        listOperations.rightPush("test", "4");

        listOperations.getOperations().expire("key", 5, TimeUnit.DAYS);
        /**
         * Hash -
         */
        hashOperations.put("test:user:chul", "name", "chul");
        hashOperations.put("test:user:chul", "age", "28");
        /**
         * set -
         */
        setOperations.add("test:user:chulm", "a");
        setOperations.add("test:user:chulm", "b");
        setOperations.add("test:user:chulm", "c");

        /**
         * sorted set -
         */
        zSetOperations.add("test:user:chul1", "a", 1);
        zSetOperations.add("test:user:chul1", "b", 2);
        zSetOperations.add("test:user:chul1", "c", 3);
        zSetOperations.add("test:user:chul1", "d", 4);

    }

    @Test
    public void jedisTest() {
        System.out.println(valueOperations.get("key"));
        System.out.println(listOperations.leftPop(("test")));
        System.out.println(hashOperations.multiGet("test:user:chul", new HashSet<>()));
        System.out.println(setOperations.pop("test:user:chulm"));
        System.out.println(zSetOperations.range("test:user:chul1", 1, 2));
    }

}
