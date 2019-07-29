package com.chulm.shorturl.test;

import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.ShortUrlRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;
import rx.observers.TestSubscriber;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { ShortUrlRepository.class})
@TestPropertySource("classpath:application-test.properties")
@JdbcTest
public class RepositoryTests {

    @Autowired
    private ShortUrlRepository repository;

    @Test
    public void saveTest(){
        ShortUrl url = ShortUrl.builder().link("http://www.naver.com").status(200).build();
        Observable<Integer> observable = repository.save(url);

        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertCompleted();
        subscriber.assertValueCount(1);
        subscriber.assertValues(1);
    }

    @Test
    public void findTest(){
        String url = "http://www.naver.com";

        Observable<ShortUrl> observable = repository.findByUrl(url);
        TestSubscriber subscriber = new TestSubscriber();
        observable.subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertCompleted();
        subscriber.assertValueCount(1);

        ShortUrl getUrl = (ShortUrl)subscriber.getOnNextEvents().get(0);

        Assert.assertEquals(getUrl.getLink(), url);
        Assert.assertEquals(getUrl.getStatus(), HttpStatus.MOVED_PERMANENTLY.value());
        Assert.assertEquals(getUrl.getId(), 1l);

    }


}
