package com.clientless.songmanagement.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String request) {
        super(request + " not found");
    }
}
