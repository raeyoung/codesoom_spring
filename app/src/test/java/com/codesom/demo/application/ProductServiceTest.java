// 1. getProducts -> 목록
// 2. getProduct -> 상세조회
// 3. createProduct -> 상품 추가
// 4. updateProduct -> 상품 수정
// 5. deleteProduct -> 상품 삭제

package com.codesom.demo.application;

import com.codesom.demo.ProductNotFoundException;
import com.codesom.demo.domain.Product;
import com.codesom.demo.domain.ProductRepository;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductServiceTest {
    private ProductService productService;
    private ProductRepository productRepository = mock(ProductRepository.class);;

    @BeforeEach
    void setUp() {
        Product product = new Product(1L, "쥐돌이", "냥이월드", 5000);
        productService = new ProductService(productRepository);

        given(productRepository.findAll()).willReturn(List.of(product));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).will(invocation -> {
            Product source = invocation.getArgument(0);
            return new Product(
                    2L,
                    source.getName(),
                    source.getMaker(),
                    source.getPrice()
            );
        });

    }

    @Test
    void getProducts() {
        List<Product> products = productService.getProducts();
        assertThat(products).isNotEmpty();

        Product product = products.get(0);

        assertThat(product.getName()).isEqualTo("쥐돌이");
    }

    @Test
    void getProductsWithNoProduct() {
        given(productRepository.findAll()).willReturn(List.of());

        assertThat(productService.getProducts()).isEmpty();
    }

    @Test
    void getProductWithExistedId() {
        Product product = productService.getProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐돌이");
    }

    @Test
    void getProductWithNotExistedId() {
        assertThatThrownBy(() -> productService.getProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);

    }

    @Test
    void createProduct() {
        Product product = productService.createProduct(new Product("멍냥이", "냥이월드", 5000));

        verify(productRepository).save(any(Product.class));

        assertThat(product.getId()).isEqualTo(2L);

    }

    @Test
    void updateProductWithExistedProduct() {
        Product source = new Product("멍냥이", "냥이월드", 5000);
        Product product = productService.updateProduct(1L, source);

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("멍냥이");
        assertThat(product.getMaker()).isEqualTo("냥이월드");
    }

    @Test
    void updateProductWithNotExistedProduct() {
        Product source = new Product("멍냥이", "냥이월드", 5000);

        assertThatThrownBy(() -> productService.updateProduct(1000L, source))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProductWithExisted() {
        productService.deleteProduct(1L);

        verify(productRepository).delete(any(Product.class));
    }

    @Test
    void deleteProductWithNotExisted() {
        assertThatThrownBy(() -> productService.deleteProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}