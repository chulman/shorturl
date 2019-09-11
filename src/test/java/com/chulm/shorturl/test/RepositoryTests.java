package com.chulm.shorturl.test;

import com.chulm.shorturl.config.LettuceConfig;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.domain.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigurationPackage
@TestPropertySource(locations = "classpath:application-test.properties")
public class RepositoryTests {

    @Autowired
    UrlRepository repository;

    @Test
    public void FindByIdTest(){
        ShortUrl url = repository.findByUrl("http://www.naver.com");

        assertNotNull(url);
        assertThat(url.getId(), is(1l));
    }
}
