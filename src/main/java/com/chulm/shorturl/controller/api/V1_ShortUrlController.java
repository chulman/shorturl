package com.chulm.shorturl.controller.api;


import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/v1/short-url")
public class V1_ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping(value = "/{code}")
    public CachedUrl get(@Valid @PathVariable @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String code) {
        return shortUrlService.get(code);
    }

    @GetMapping(value = "/get/url")
    public CachedUrl getOrSave(@Valid @RequestParam @Pattern(regexp = RegexUtil.URL_REGEX) String url) {
        return shortUrlService.getOrSave(url);
    }
}
