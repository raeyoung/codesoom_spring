package com.codesom.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void changeWith() {
        User user = User.builder().build();

        user.changeWith(User.builder()
                .name("test")
                .password("test1234")
                .build());

        assertThat(user.getName()).isEqualTo("test");
        assertThat(user.getPassword()).isEqualTo("test1234");
    }
    @Test
    void destroy() {
        User user = User.builder().build();
        assertThat(user.isDeleted()).isFalse();

        user.destroy();

        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void authenticate() {
        User user = User.builder()
                .password("test")
                .build();
        assertThat(user.authenticate("test")).isTrue();
        assertThat(user.authenticate("1234")).isFalse();
    }

    @Test
    void authenticateWithDeletedUser() {
        User user = User.builder()
                .password("test")
                .deleted(true)
                .build();
        assertThat(user.authenticate("test")).isFalse();
        assertThat(user.authenticate("1234")).isFalse();
    }

}