package com.xpertgroup.cats.exception;

/**
 * Custom exception for when a user already exists
 */
public class UserExistsException extends RuntimeException {
    
    public UserExistsException(String message) {
        super(message);
    }
    
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}