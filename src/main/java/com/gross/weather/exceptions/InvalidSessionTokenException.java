package com.gross.weather.exceptions;

public class InvalidSessionTokenException extends RuntimeException {
    public InvalidSessionTokenException(String token) {
        super("Invalid session token: " + token);
    }
}