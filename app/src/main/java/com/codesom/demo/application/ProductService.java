// 1. getProducts -> 목록
// 2. getProduct -> 상세조회
// 3. createProduct -> 상품 추가
// 4. updateProduct -> 상품 수정
// 5. deleteProduct -> 상품 삭제

package com.codesom.demo.application;

import com.codesom.demo.ProductNotFoundException;
import com.codesom.demo.domain.Product;
import com.codesom.demo.domain.ProductRepository;
import com.codesom.demo.dto.ProductData;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    private final ProductRepository productRepository;

    public ProductService(Mapper dozerMapper, ProductRepository productRepository) {
        this.mapper = dozerMapper ;
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    public Product createProduct(ProductData productData) {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        Product product = mapper.map(productData, Product.class);
//        Product product = Product.builder()
//                .name(productData.getName())
//                .maker(productData.getMaker())
//                .price(productData.getPrice())
//                .build();
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductData productData) {
        // product 정보 얻기
        // product 정보 변경하기
        Product product = findProduct(id);

        product.change(productData.getName(), productData.getMaker(), productData.getPrice());

        return product;
    }

    public Product deleteProduct(long id) {
        Product product = findProduct(id);

        productRepository.delete(product);
        return product;
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

}
