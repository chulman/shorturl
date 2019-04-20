package com.chulm.shorturl.controller.web;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.exception.ResourceNotFoundException;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Pattern;

@RestController
public class ShortUrlController {

    @Value("${short-url.service.url}")
    private String domain;

    @Autowired
    private ShortUrlService shortUrlService;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity redirect(@RequestParam @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String url) {
        CachedUrl cachedUrl = shortUrlService.getAndSave(url);
        System.err.println("aaa");
        if (cachedUrl != null) {
            ShortUrl shortUrl = cachedUrl.getShortUrl();

            if (shortUrl != null) {
                HttpStatus status = HttpStatus.valueOf(shortUrl.getStatus());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, shortUrl.getLink());
                return new ResponseEntity(headers, status);
            }
        }else {
            throw new ResourceNotFoundException("Page not found: /" + url);
        }
        return null;
    }
}
