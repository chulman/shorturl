package com.chulm.shorturl.test.api;


import com.chulm.shorturl.domain.model.CachedUrl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class V1_ApiTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getUrlTest() throws Exception {

        String expectedLink = "http://www.naver.com";

        MvcResult result = mockMvc.perform(get("/api/v1/short-url/get/url").param("url", expectedLink))
                .andExpect(status().isOk())
                .andReturn();

        CachedUrl cachedUrl = mapper.readValue(result.getResponse().getContentAsString(), CachedUrl.class);

        assertThat(cachedUrl.getCode(), is("B"));
        assertThat(cachedUrl.getShortUrl().getLink(), is(expectedLink));
        assertThat(cachedUrl.getShortUrl().getStatus(), is(HttpStatus.MOVED_PERMANENTLY.value()));
    }

    @Test
    public void getCodeTest() throws Exception {

        String expectedLink = "http://www.naver.com";

        MvcResult result = mockMvc.perform(get("/api/v1/short-url/B"))
                .andExpect(status().isOk())
                .andReturn();

        CachedUrl cachedUrl = mapper.readValue(result.getResponse().getContentAsString(), CachedUrl.class);

        assertThat(cachedUrl.getCode(), is("B"));
        assertThat(cachedUrl.getShortUrl().getLink(), is(expectedLink));
        assertThat(cachedUrl.getShortUrl().getStatus(), is(HttpStatus.MOVED_PERMANENTLY.value()));
    }
}
