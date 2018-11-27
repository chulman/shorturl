package com.chulm.shorturl.util;

public class RegexUtil {

    public static final String NUMERIC_AND_ALPHABETIC_REGEX = "[0-9a-zA-Z]*";
    public static final String URL_REGEX="^(http|https?):\\/\\/([a-z0-9-]+\\.)+[a-z0-9]{2,4}.*$";
}
