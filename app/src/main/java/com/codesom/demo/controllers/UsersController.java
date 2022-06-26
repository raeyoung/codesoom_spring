// 1. 가입 -> POST /users => 가입 정보 (DTO) - email 이 unique key
// 2. 목록, 상세보기 => ADMIN!
// 3. 사용자 정보 갱신 -> PUT/PATCH /users/{id} => 정보 갱신 (DTO) -> 이름만!
// => 권한 확인 Authorization
// 4. 탈퇴 -> DELETE /users/{id}
// => 권한 확인 Authorization
package com.codesom.demo.controllers;

import com.codesom.demo.application.UserService;
import com.codesom.demo.domain.User;
import com.codesom.demo.dto.UserModificationData;
import com.codesom.demo.dto.UserResultData;
import com.codesom.demo.dto.UserRegistrationData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;



    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData userRegistrationData) {
        User user = userService.registerUser(userRegistrationData);
        return getUserResultData(user);
    }

    @PatchMapping("{id}")
    UserResultData update(@PathVariable Long id, @RequestBody @Valid UserModificationData modificationData) {
        User user = userService.updateUser(id, modificationData);
        return getUserResultData(user);
    }

    private UserResultData getUserResultData(User user) {
        if(user == null) return null;
        return UserResultData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @DeleteMapping("{id}")
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
