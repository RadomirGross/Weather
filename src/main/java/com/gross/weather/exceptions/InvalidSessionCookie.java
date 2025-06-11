package com.gross.weather.exceptions;


public class InvalidSessionCookie extends RuntimeException {
    public InvalidSessionCookie(String message) {
        super(message);
    }

    public InvalidSessionCookie (String message, Throwable cause) {
        super(message, cause);
    }
}
