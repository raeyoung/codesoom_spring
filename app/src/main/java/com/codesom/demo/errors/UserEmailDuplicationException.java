package com.codesom.demo.errors;

public class UserEmailDuplicationException extends RuntimeException{
    public UserEmailDuplicationException(String email) {
        super("User Email is already existed:" + email);
    }
}
