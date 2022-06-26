package com.codesom.demo.errors;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User is not found" + id);
    }
}
