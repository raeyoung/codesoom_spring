package com.codesom.demo.application;

import com.codesom.demo.errors.UserEmailDuplicationException;
import com.codesom.demo.errors.UserNotFoundException;
import com.codesom.demo.domain.User;
import com.codesom.demo.domain.UserRepository;
import com.codesom.demo.dto.UserModificationData;
import com.codesom.demo.dto.UserRegistrationData;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper, UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegistrationData registrationData) {
        String email = registrationData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        User user = mapper.map(registrationData, User.class);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);
        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    public User deleteUser(long id) {
        User user = findUser(id);
        user.destroy();

        return user;
    }

    private User findUser(long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
