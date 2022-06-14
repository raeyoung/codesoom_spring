package com.codesom.demo.controllers;

import com.codesom.demo.UserNotFoundException;
import com.codesom.demo.application.UserService;
import com.codesom.demo.domain.User;
import com.codesom.demo.dto.UserModificationData;
import com.codesom.demo.dto.UserRegistrationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(UsersController.class)
class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        given(userService.registerUser(any(UserRegistrationData.class)))
                .will(invocation -> {
                    UserRegistrationData registrationData = invocation.getArgument(0);
                    User user = User.builder()
                            .id(13L)
                            .name(registrationData.getName())
                            .email(registrationData.getEmail())
                            .build();
                    return user;
                });
        given(userService.updateUser(eq(1L), any(UserModificationData.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserModificationData modificationData = invocation.getArgument(1);
                    User user = User.builder()
                            .id(id)
                            .email("tester@example.com")
                            .name(modificationData.getName())
                            .build();
                    return user;
                });

        given(userService.updateUser(eq(100L), any(UserModificationData.class)))
                .willThrow(new UserNotFoundException(100L));

        given(userService.deleteUser(100L))
                .willThrow(new UserNotFoundException(100L));
    }

    @Test
    void registerUserWithValidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"name\":\"Tester\",\"password\":\"test\"}")
        ).andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"id\":13")))
                .andExpect(content().string(containsString("\"email\":\"tester@example.com\"")))
                .andExpect(content().string(containsString("\"name\":\"Tester\"")));

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @Test
    void registerUserWithInValidAttributes() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                ).andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithValidAttributes() throws Exception {
        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\",\"password\":\"test1234\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"name\":\"test\"")));

        verify(userService).updateUser(eq(1L), any(UserModificationData.class));
    }

    @Test
    void updateUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"password\":\"\"}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithNotExistedId() throws Exception {
        mockMvc.perform(patch("/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TEST\",\"password\":\"TEST\"}")
                )
                .andExpect(status().isNotFound());
        verify(userService).updateUser(eq(100L), any(UserModificationData.class));
    }

    @Test
    void destroyWithExistedId() throws Exception {
        mockMvc.perform(delete("/users/1")
                )
                .andExpect(status().isOk());
        verify(userService).deleteUser(1L);
    }

    @Test
    void destroyWithNotExistedId() throws Exception {
        mockMvc.perform(delete("/users/100")
                )
                .andExpect(status().isNotFound());
        verify(userService).deleteUser(100L);
    }
}