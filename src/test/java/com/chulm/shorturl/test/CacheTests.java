package com.chulm.shorturl.test;

import com.chulm.shorturl.config.ApplicationConfig;
import com.chulm.shorturl.config.LettuceConfig;
import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.chulm.shorturl.util.Base62Codec;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {LettuceConfig.class, ApplicationConfig.class})
@TestPropertySource("classpath:application-test.properties")
public class CacheTests {

    @Autowired
    ShortUrlCacheRepository cacheRepository;

//    @MockBean
//    private StatefulRedisConnection<String, String> stringStringStatefulRedisConnection;

    @Test
    public void set_test() {

        ShortUrl url = new ShortUrl(1, "http://www.naver.com", 301);

        Observable<String> observable = cacheRepository.setCachedShortUrl(url);
        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertReceivedOnNext(Arrays.asList("OK"));
    }


    // avoid npe with in service nonNull
    @Test(expected = NullPointerException.class)
    public void set_test_expected_NPE() {
        Observable<String> observable = cacheRepository.setCachedShortUrl(null);
        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();
    }

    @Test()
    public void get_test(){

        ShortUrl expectedShortUrl = ShortUrl.builder().status(301).link("http://www.naver.com").id(1).build();


        Observable<ShortUrl> observable = cacheRepository.get(Base62Codec.encode(1));
        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);

        subscriber.assertReceivedOnNext(Arrays.asList(expectedShortUrl));
    }


    @Test(expected = NullPointerException.class)
    public void get_test_expected_NPE(){

        Observable<ShortUrl> observable = cacheRepository.get(null);
        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();

    }
}
