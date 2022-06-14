package com.codesom.demo.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 웹을 통해서 외부랑 소통하는 용도로만 사용
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private Long id;

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("maker")
    private String maker;

    @NotNull
    @Mapping("price")
    private Integer price;

    @Mapping("imageUrl")
    private String imageUrl;
}
