// REST API
// 1. GET /products -> 목록
// 2. GET /products/{id} -> 상세조회
// 3. POST /products -> 상품 추가
// 4. PUT/PATCH /products/{id} -> 상품 수정
// 5. DELETE /products/{id} -> 상품 삭제

package com.codesom.demo.controllers;

import com.codesom.demo.application.ProductService;
import com.codesom.demo.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("{id}")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
