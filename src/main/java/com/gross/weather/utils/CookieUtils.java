package com.gross.weather.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import java.util.Optional;
import java.util.UUID;

public class CookieUtils {
    public static Optional<Cookie> extractCookieFromRequest(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie == null) {
            return Optional.empty();
        }else  {
            return Optional.of(cookie);
        }
    }

    public static Optional<UUID> extractUuidFromCookie(HttpServletRequest request, String cookieName) {
        Optional<Cookie> cookieOptional = extractCookieFromRequest(request, cookieName);
        if (cookieOptional.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(cookieOptional.get().getValue()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }
}
