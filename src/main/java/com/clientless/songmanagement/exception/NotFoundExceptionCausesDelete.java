package com.clientless.songmanagement.exception;

public class NotFoundExceptionCausesDelete extends RuntimeException{

    public NotFoundExceptionCausesDelete(String request1, String request2) {
        super(request1 + " not found because was " + request2 );
    }
}
