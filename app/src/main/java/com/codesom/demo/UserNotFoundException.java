package com.codesom.demo;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User is not found" + id);
    }
}
