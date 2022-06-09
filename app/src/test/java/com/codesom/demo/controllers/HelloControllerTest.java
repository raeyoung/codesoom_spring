package com.codesom.demo.controllers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {
    @Test
    void sayHello() {
        HelloController controller = new HelloController();
        // TODO : result -> Hello, world!
        // assertEquals("Hello, world!", result);
        assertThat(controller.sayHello()).isEqualTo("Hello, world!");
    }
}