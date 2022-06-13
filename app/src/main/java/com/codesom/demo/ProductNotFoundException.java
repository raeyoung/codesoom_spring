package com.codesom.demo;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id) {
        super("Product not found:" + id);
    }
}
