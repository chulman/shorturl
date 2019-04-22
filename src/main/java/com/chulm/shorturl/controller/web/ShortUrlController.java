package com.chulm.shorturl.controller.web;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.exception.ResourceNotFoundException;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.RegexUtil;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
public class ShortUrlController {


    @Value("${short-url.service.url}")
    private String domain;

    @Autowired
    private ShortUrlService shortUrlService;


    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity redirect(@PathVariable @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String code) {
        CachedUrl cachedUrl = shortUrlService.get(code);
        if (cachedUrl != null) {
            ShortUrl shortUrl = cachedUrl.getShortUrl();

            if (shortUrl != null) {
                HttpStatus status = HttpStatus.valueOf(shortUrl.getStatus());
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, shortUrl.getLink());
                return new ResponseEntity(headers, status);
            }
        }else {
            throw new ResourceNotFoundException("Page not found: /" + code);
        }
        return null;
    }
}
