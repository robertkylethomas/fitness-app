package com.udacity.fitchallenge.exception;

/**
 * Exception thrown when a requested resource is not found
 * Students can use this as-is
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
