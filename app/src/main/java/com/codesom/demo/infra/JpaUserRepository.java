package com.codesom.demo.infra;

import com.codesom.demo.domain.User;
import com.codesom.demo.domain.UserRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaUserRepository extends UserRepository, CrudRepository<User, Long> {

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeletedIsFalse(Long id);

    Optional<User> findByEmail(String email);
}
