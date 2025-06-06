package com.company.product.service;

import com.company.product.dto.external.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 *
 * @author ruiz_
 */

@Service
public class FakeStoreService {

    private final WebClient webClient;

    @Autowired
    public FakeStoreService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<FakeStoreProductDto> getAllProducts() {
        return webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(FakeStoreProductDto.class);
    }

    public Mono<FakeStoreProductDto> getProductById(Long id) {
        return webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(FakeStoreProductDto.class);
    }

    public Mono<FakeStoreProductDto> createProduct(FakeStoreProductDto product) {
        return webClient.post()
                .uri("/products")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(FakeStoreProductDto.class);
    }

    public Mono<FakeStoreProductDto> updateProduct(Long id, FakeStoreProductDto product) {
        return webClient.put()
                .uri("/products/{id}", id)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(FakeStoreProductDto.class);
    }

    public Mono<FakeStoreProductDto> deleteProduct(Long id) {
        return webClient.delete()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(FakeStoreProductDto.class);
    }
}
