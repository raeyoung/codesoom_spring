package com.codesom.demo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void creationWithBuilder() {
//        Product product = new Product("쥐돌이", "냥이월드", 5000);

        //lombok 사용
        Product product = Product.builder()
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        assertThat(product.getName()).isEqualTo("쥐돌이");
        assertThat(product.getMaker()).isEqualTo("냥이월드");
        assertThat(product.getPrice()).isEqualTo(5000);
        assertThat(product.getImageUrl()).isNull();
    }

    @Test
    void change() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        Product source = Product.builder()
                .id(1L)
                .name("멈멈이")
                .maker("멍이월드")
                .price(1000)
                .build();

        product.change("멈멈이", "멍이월드", 1000);

        assertThat(product.getName()).isEqualTo("멈멈이");
        assertThat(product.getMaker()).isEqualTo("멍이월드");
    }

}