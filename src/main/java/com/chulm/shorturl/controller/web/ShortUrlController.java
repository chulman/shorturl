package com.chulm.shorturl.controller.web;

import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.exception.ResourceNotFoundException;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.Base62Codec;
import com.chulm.shorturl.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
public class ShortUrlController {

    @Value("${short-url.service.url}")
    private String domain;

    @Autowired
    private ShortUrlService shortUrlService;

    //curl -d '{"url": "http://www.nate.com"}' -x POST http://localhost:8080/
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@RequestParam @Pattern(regexp = RegexUtil.URL_REGEX) String url){
        System.err.println(url);
        CachedUrl cachedUrl = shortUrlService.saveDB(url);
        return cachedUrl.getCode();
    }

//    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
//    public ResponseEntity redirect(@PathVariable @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String code) {
//        CachedUrl cachedUrl = shortUrlService.get(code);
//
//        if(cachedUrl != null){
//            ShortUrl shortUrl = cachedUrl.getShortUrl();
//
//            if (shortUrl != null) {
//                HttpStatus status = HttpStatus.valueOf(shortUrl.getStatus());
//                HttpHeaders headers = new HttpHeaders();
//                headers.add(HttpHeaders.LOCATION, shortUrl.getLink());
//                return new ResponseEntity(headers, status);
//            }
//            else {
//                //TODO link to error page
//                throw new ResourceNotFoundException("Page not found: /" + code);
//            }
//        }
//        //TODO link to error page
//        return null;
//    }
}
