package com.chulm.shorturl.domain.model;

public class CachedUrl {

    private String code;
    private ShortUrl shortUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public ShortUrl getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(ShortUrl shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Override
    public String toString() {
        return "CachedUrl{" +
                "code='" + code + '\'' +
                ", shortUrl=" + shortUrl +
                '}';
    }
}
