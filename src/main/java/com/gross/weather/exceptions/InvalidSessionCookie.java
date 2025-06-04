package com.gross.weather.exceptions;

import jakarta.servlet.http.Cookie;

public class InvalidSessionCookie extends RuntimeException {
    public InvalidSessionCookie(String message) {
        super(message);
    }

    public InvalidSessionCookie (String message, Throwable cause) {
        super(message, cause);
    }
}
