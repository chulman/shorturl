package com.chulm.shorturl.test;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.repository.ShortUrlCacheRepository;
import com.chulm.shorturl.domain.repository.UrlRepository;
import com.chulm.shorturl.service.ShortUrlService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTests {
    @Mock
    private ShortUrlCacheRepository cacheRepository;
    @Mock
    private UrlRepository urlRepository;
    @InjectMocks
    private ShortUrlService shortUrlService;

    @Test
    public void getTest(){
        CachedUrl cachedUrl = shortUrlService.get("A");
        Assert.assertNull(cachedUrl);
    }

    @Test
    public void validateTest(){
        CachedUrl cachedUrl = shortUrlService.validate("A");
        Assert.assertNull(cachedUrl);
    }

    @Test
    public void getOrSaveTest(){
        CachedUrl cachedUrl = shortUrlService.getOrSave("http://www.naver.com");
        Assert.assertNull(cachedUrl);
    }

}
