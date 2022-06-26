package com.codesom.demo.controllers;

import com.codesom.demo.application.AuthenticationService;
import com.codesom.demo.errors.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@example.com", "test")).willReturn("a.b.c.");
        given(authenticationService.login("bad@example.com", "test")).willThrow(
                new LoginFailException("bad@example.com"));
        given(authenticationService.login("tester@example.com", "1234")).willThrow(
                new LoginFailException("bad@example.com"));
    }

    @Test
    void loginWithRightEmailAndPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }

    @Test
    void loginWithWrongEmail() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bad@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithWrongPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\",\"password\":\"1234\"}"))
                .andExpect(status().isBadRequest());
    }
}