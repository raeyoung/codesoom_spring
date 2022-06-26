package com.codesom.demo.application;

import com.codesom.demo.domain.User;
import com.codesom.demo.domain.UserRepository;
import com.codesom.demo.errors.InvalidTokenException;
import com.codesom.demo.errors.LoginFailException;
import com.codesom.demo.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {
    private static final String SECRET = "miffymiffymiffymiffymiffymiffymiffymiffy";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.l6Tp_vtanMzAt_PhekZ6zOBhFH0V3bSpMNbag1A9RUY";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.l6Tp_vtanMzAt_PhekZ6zOBhFH0V3bSpMNbag1A9RUY000";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(userRepository, jwtUtil);

        User user = User.builder()
                .password("test")
                .build();
        given(userRepository.findByEmail("tester@example.com")).willReturn(Optional.of(user));
    }

    @Test
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService.login("tester@example.com", "test");

        // 꼼수로 사용할 수 있는데 test 코드 실행시 해당 token 이 저명히 보임
//        assertThat(accessToken).contains(".xxx");
        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmail("tester@example.com");
    }

    @Test
    void loginWithWrongEmail() {
        assertThatThrownBy(() -> authenticationService.login("bad@example.com", "test"))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("bad@example.com");
    }

    @Test
    void loginWithWrongPassword() {
        assertThatThrownBy(() -> authenticationService.login("tester@example.com", "1234"))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("tester@example.com");
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId =  authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void parseTokenWithEmptyToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken("  "))
                .isInstanceOf(InvalidTokenException.class);
    }
}