// REST API
// 1. GET /products -> 목록
// 2. GET /products/{id} -> 상세조회
// 3. POST /products -> 상품 추가
// 4. PUT/PATCH /products/{id} -> 상품 수정
// 5. DELETE /products/{id} -> 상품 삭제

package com.codesom.demo.controllers;

import com.codesom.demo.application.AuthenticationService;
import com.codesom.demo.application.ProductService;
import com.codesom.demo.domain.Product;
import com.codesom.demo.dto.ProductData;
import com.codesom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private AuthenticationService authenticationService;


    public ProductController(ProductService productService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
//        Product product = new Product(1L, "쥐돌이", "냥이월드", 5000);
//
//        return List.of(product);
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
//        Product product = new Product(1L, "쥐돌이", "냥이월드", 5000);
//
//        return List.of(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid ProductData productData) {

        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id, @RequestBody @Valid ProductData productData) {
        String accessToken = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(accessToken);

        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleMissingRequestHeaderException() {

    }
}
