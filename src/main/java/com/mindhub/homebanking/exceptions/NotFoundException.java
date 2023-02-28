package com.mindhub.homebanking.exceptions;

public class NotFoundException extends ResponseException{
    public NotFoundException(String message) {
        super(message, 404);
    }
}
