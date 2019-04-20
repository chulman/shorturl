package com.chulm.shorturl.controller.api;


import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/v1/short-url")
public class V1_ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping(value = "/{code}")
    public CachedUrl getShortUrl_path(@PathVariable @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String code) {
        return shortUrlService.get(code);
    }
}
