// REST API
// 1. GET /products -> 목록
// 2. GET /products/{id} -> 상세조회
// 3. POST /products -> 상품 추가
// 4. PUT/PATCH /products/{id} -> 상품 수정
// 5. DELETE /products/{id} -> 상품 삭제

package com.codesom.demo.controllers;

import com.codesom.demo.ProductNotFoundException;
import com.codesom.demo.application.ProductService;
import com.codesom.demo.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Product product = new Product(1L, "쥐돌이", "냥이월드", 5000);

        given(productService.getProducts()).willReturn(List.of(product));
        given(productService.getProduct(1L)).willReturn(product);
        given(productService.getProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
        given(productService.createProduct(any(Product.class))).willReturn(product);
        given(productService.updateProduct(eq(1L), any(Product.class)))
                .will(invocation -> {
                    Product source = invocation.getArgument(1);
                    Long id = invocation.getArgument(0);
                    return new Product(
                            id,
                            source.getName(),
                            source.getMaker(),
                            source.getPrice()
                    );
                });
        given(productService.updateProduct(eq(1000L), any(Product.class)))
                .willThrow(new ProductNotFoundException(1000L));
        given(productService.deleteProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));

    }

    @Test
    void detailWithExitedProduct() throws Exception {
        mockMvc.perform(get("/products/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));

    }

    @Test
    void detailWithNotExitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1").accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"멍냥이\",\"maker\":\"냥이월드\",\"price\":5000}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("멍냥이")));

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"멍냥이\",\"maker\":\"냥이월드\",\"price\":5000}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void destroyWithExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1")).andExpect(status().isOk());

        verify(productService).deleteProduct(1L);
    }

    @Test
    void destroyWithNotExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1000")).andExpect(status().isNotFound());

        verify(productService).deleteProduct(1000L);
    }

}