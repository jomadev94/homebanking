package com.mindhub.homebanking.exceptions;

public class BadReqException extends ResponseException{
    public BadReqException(String message) {
        super(message, 400);
    }
}
