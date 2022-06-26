package com.codesom.demo.errors;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String token) {
        super("Invalid token : " + token);
    }
}
