
package com.company.carts.service;

import com.company.carts.dto.external.FakeStoreCartsDto;
import com.company.carts.dto.external.FakeStoreProductDto;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author ruiz_
 */
@Slf4j
@Service
public class FakeStoreService {

    private final WebClient webClient;

    @Autowired
    public FakeStoreService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<FakeStoreCartsDto> getAllCarts() {
        return webClient.get()
                .uri("/carts")
                .retrieve()
                .bodyToFlux(FakeStoreCartsDto.class);
    }

    public Mono<FakeStoreCartsDto> getCartById(Long id) {
        return webClient.get()
                .uri("/carts/{id}", id)
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class);
    }

    public Mono<FakeStoreCartsDto> createCart(FakeStoreCartsDto cart) {
        return webClient.post()
                .uri("/carts")
                .bodyValue(cart)
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class);
    }

    public Mono<FakeStoreCartsDto> updateCart(Long id, FakeStoreCartsDto cart) {
        return webClient.put()
                .uri("/carts/{id}", id)
                .bodyValue(cart)
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class);
    }

    public Mono<FakeStoreCartsDto> deleteCart(Long id) {
        return webClient.delete()
                .uri("/carts/{id}", id)
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class);
    }

    public Flux<FakeStoreCartsDto> getCartsByUserId(Long userId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/carts")
                .queryParam("userId", userId)
                .build())
                .retrieve()
                .bodyToFlux(FakeStoreCartsDto.class)
                .onErrorResume(e -> {
                    log.error("Error fetching carts for user ID: {}", userId, e);
                    return Flux.empty();
                });
    }

    public Mono<FakeStoreCartsDto> addProductToCart(Long cartId, FakeStoreProductDto product) {
        return webClient.patch()
                .uri("/carts/{id}", cartId)
                .bodyValue(Collections.singletonMap("addProduct", product))
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class)
                .onErrorResume(e -> {
                    log.error("Error adding product to cart ID: {}", cartId, e);
                    return Mono.error(new RuntimeException("Failed to add product to cart", e));
                });
    }

    public Mono<FakeStoreCartsDto> removeProductFromCart(Long cartId, Long productId) {
        return webClient.patch()
                .uri("/carts/{id}", cartId)
                .bodyValue(Collections.singletonMap("removeProduct", productId))
                .retrieve()
                .bodyToMono(FakeStoreCartsDto.class)
                .onErrorResume(e -> {
                    log.error("Error removing product {} from cart ID: {}", productId, cartId, e);
                    return Mono.error(new RuntimeException("Failed to remove product from cart", e));
                });
    }
}
