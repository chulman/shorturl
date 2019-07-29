package com.chulm.shorturl.controller.api;


import com.chulm.shorturl.domain.model.CachedUrl;
import com.chulm.shorturl.domain.model.ShortUrl;
import com.chulm.shorturl.service.ShortUrlService;
import com.chulm.shorturl.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/v1/short-url")
public class V1_ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping(value = "/{code}")
    public DeferredResult<CachedUrl> get(@Valid @PathVariable @Pattern(regexp = RegexUtil.NUMERIC_AND_ALPHABETIC_REGEX) String code) {
        return toDeferredResult(shortUrlService.get(code));
    }

    @GetMapping(value = "/get/url")
    public DeferredResult<CachedUrl> save(@Valid @RequestParam @Pattern(regexp = RegexUtil.URL_REGEX) String url) {
        return toDeferredResult(shortUrlService.save(url));
    }

    private static <T> DeferredResult<T> toDeferredResult(Observable<T> observable) {
        DeferredResult<T> deferredResult = new DeferredResult<>();
        observable.subscribe(deferredResult::setResult, deferredResult::setErrorResult);
        return deferredResult;
    }
}
