package com.codesom.demo.application;

import com.codesom.demo.errors.UserEmailDuplicationException;
import com.codesom.demo.errors.UserNotFoundException;
import com.codesom.demo.domain.User;
import com.codesom.demo.domain.UserRepository;
import com.codesom.demo.dto.UserModificationData;
import com.codesom.demo.dto.UserRegistrationData;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {
    private UserService userService;
    private final UserRepository userRepository = mock(UserRepository.class);

    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final Long DELETED_USER_ID = 200L;

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userService = new UserService(mapper, userRepository);

        given(userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS)).willReturn(true);

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);
            return User.builder()
                    .name(source.getName())
                    .build();
        });

        given(userRepository.findByIdAndDeletedIsFalse(1L)).willReturn(Optional.of(User.builder()
                .id(1L)
                .email(EXISTED_EMAIL_ADDRESS)
                .name("test")
                .password("test1234")
                .build()));

        given(userRepository.findByIdAndDeletedIsFalse(100L)).willReturn(Optional.empty());

        given(userRepository.findByIdAndDeletedIsFalse(DELETED_USER_ID)).willReturn(Optional.empty());
    }

    @Test
    void registerUser() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email("tester@example.com")
                .name("Tester")
                .password("test")
                .build();
        User user = userService.registerUser(registrationData);

        assertThat(user.getName()).isEqualTo("Tester");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserWithDuplicatedEmail() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email(EXISTED_EMAIL_ADDRESS)
                .name("Tester")
                .password("test")
                .build();

        assertThatThrownBy(() -> userService.registerUser(registrationData))
                .isInstanceOf(UserEmailDuplicationException.class);

        verify(userRepository).existsByEmail(EXISTED_EMAIL_ADDRESS);
    }

    @Test
    void updateUserWithExistedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("test")
                .password("test1234")
                .build();
        User user = userService.updateUser(1L, modificationData);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo(EXISTED_EMAIL_ADDRESS);
        assertThat(user.getName()).isEqualTo("test");

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void updateUserWithNotExistedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("test")
                .password("test1234")
                .build();

        assertThatThrownBy(() ->userService.updateUser(100L, modificationData))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }

    @Test
    void updateUserWithDeletedId() {
        UserModificationData modificationData = UserModificationData.builder()
                .name("test")
                .password("test1234")
                .build();

        assertThatThrownBy(() -> userService.updateUser(DELETED_USER_ID, modificationData))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
    }

    @Test
    void deleteUserWithExistedId() {
        User user = userService.deleteUser(1L);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.isDeleted()).isTrue();

        verify(userRepository).findByIdAndDeletedIsFalse(1L);
    }

    @Test
    void deleteUserWithNotExistedId() {
        assertThatThrownBy(() -> userService.deleteUser(100L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(100L);
    }

    @Test
    void deleteUserWithDeletedId() {
        assertThatThrownBy(() -> userService.deleteUser(DELETED_USER_ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdAndDeletedIsFalse(DELETED_USER_ID);
    }
}