package com.mindhub.homebanking.exceptions;

public class ForbiddenException extends ResponseException {
    public ForbiddenException(String message) {
        super(message, 403);
    }
}
