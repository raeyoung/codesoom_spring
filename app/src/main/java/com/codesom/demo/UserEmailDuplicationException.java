package com.codesom.demo;

public class UserEmailDuplicationException extends RuntimeException{
    public UserEmailDuplicationException(String email) {
        super("User Email is already existed:" + email);
    }
}
