package com.booking.KBookin.exception;

public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
     public VerificationException(String message,Exception cause) {
        super(message);
    }
}
