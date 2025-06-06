package com.company.product.controller;

import com.company.product.dto.ProductRequestDTO;
import com.company.product.dto.ProductResponseDTO;
import com.company.product.service.FakeStoreService;
import com.company.product.mapper.ProductMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 *
 * @author ruiz_
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final FakeStoreService fakeStoreService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductRestController(FakeStoreService fakeStoreService, ProductMapper productMapper) {
        this.fakeStoreService = fakeStoreService;
        this.productMapper = productMapper;
    }

    @GetMapping(path = "/get-all-products")
    public Mono<ResponseEntity<List<ProductResponseDTO>>> getAllProducts() {
        log.info("get-all-products");
        return fakeStoreService.getAllProducts()
                .map(productMapper::fakeStoreToProductResponse)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponseDTO>> createProduct(@RequestBody ProductRequestDTO productRequest) {
        log.info("createProduct");
        return fakeStoreService.createProduct(productMapper.productRequestToFakeStore(productRequest))
                .map(productMapper::fakeStoreToProductResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> getProductById(@PathVariable("id") Long id) {
        log.info("getProductById , {}", id);
        return fakeStoreService.getProductById(id)
                .map(productMapper::fakeStoreToProductResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO productRequest) {
        log.info("updateProduct , {}", id);

        return fakeStoreService.updateProduct(id, productMapper.productRequestToFakeStore(productRequest))
                .map(productMapper::fakeStoreToProductResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        log.info("deleteProduct , {}", id);

        return fakeStoreService.deleteProduct(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
