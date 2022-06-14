package com.codesom.demo.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationData {
    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("password")
    @Size(min=4, max=1024)
    private String password;
}
