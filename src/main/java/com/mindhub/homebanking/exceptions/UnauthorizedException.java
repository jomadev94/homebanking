package com.mindhub.homebanking.exceptions;

public class UnauthorizedException extends ResponseException {

    public UnauthorizedException() {
        super("Not Authorized",401);
    }

}
