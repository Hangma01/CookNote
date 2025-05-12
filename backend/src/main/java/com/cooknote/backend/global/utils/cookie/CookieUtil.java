package com.cooknote.backend.global.utils.cookie;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24);
        cookie.setHttpOnly(true);

        return cookie;
    }
}