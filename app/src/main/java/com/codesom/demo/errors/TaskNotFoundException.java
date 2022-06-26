package com.codesom.demo.errors;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long id) {
        super("Task not found:" + id);
    }
}
