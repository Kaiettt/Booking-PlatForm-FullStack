package com.booking.KBookin.exception;

public class ObjectNotAvailableException extends RuntimeException {
    public ObjectNotAvailableException(String message) {
        super(message);
    }
     public ObjectNotAvailableException(String message,Exception cause) {
        super(message);
    }
}
