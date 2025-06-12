package com.company.carts.cotroller;

import com.company.carts.dto.CartProductDTO;
import com.company.carts.dto.CartsRequestDTO;
import com.company.carts.dto.CartsResponseDTO;
import com.company.carts.mapper.CartsMapper;
import com.company.carts.service.FakeStoreService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author ruiz_
 */
@Slf4j
@RestController
@RequestMapping("/carts")
@Valid
public class CartsRestController {

    private final FakeStoreService fakeStoreService;
    private final CartsMapper cartsMapper;

    @Autowired
    public CartsRestController(FakeStoreService fakeStoreService, CartsMapper cartsMapper) {
        this.fakeStoreService = fakeStoreService;
        this.cartsMapper = cartsMapper;
    }

    @GetMapping("/get-all-carts")
    public Mono<ResponseEntity<Flux<CartsResponseDTO>>> getAllCarts() {
        log.info("Getting all carts");
        return Mono.just(ResponseEntity.ok(
                fakeStoreService.getAllCarts()
                        .map(cartsMapper::fakeStoreToCartResponse)
        ));
    }

    @PostMapping
    public Mono<ResponseEntity<CartsResponseDTO>> createCart(@Valid @RequestBody CartsRequestDTO cartRequest) {
        log.info("Creating new cart for user: {}", cartRequest.getUserId());
        return fakeStoreService.createCart(cartsMapper.cartRequestToFakeStore(cartRequest))
                .map(cartsMapper::fakeStoreToCartResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .doOnSuccess(response -> log.info("Created cart with ID: {}", response.getBody().getId()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CartsResponseDTO>> getCartById( @PathVariable("id") Long id) {
        log.info("Getting cart by ID: {}", id);
        
        return fakeStoreService.getCartById(id)
                .map(cartsMapper::fakeStoreToCartResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnNext(response -> {
                    if (response.getStatusCode() == HttpStatus.OK) {
                        log.debug("Found cart with ID: {}", id);
                    } else {
                        log.warn("Cart with ID {} not found", id);
                    }
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CartsResponseDTO>> updateCart(
            @PathVariable("id") Long id,
            @RequestBody CartsRequestDTO cartRequest) {
        log.info("Updating cart with ID: {}", id);
        return fakeStoreService.updateCart(id, cartsMapper.cartRequestToFakeStore(cartRequest))
                .map(cartsMapper::fakeStoreToCartResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> {
                    if (response.getStatusCode() == HttpStatus.OK) {
                        log.info("Successfully updated cart with ID: {}", id);
                    }
                });
    }

    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<Flux<CartsResponseDTO>>> getCartsByUserId(@PathVariable("userId") Long userId) {
        log.info("Getting carts for user ID: {}", userId);
        return Mono.just(ResponseEntity.ok(
                fakeStoreService.getCartsByUserId(userId)
                        .map(cartsMapper::fakeStoreToCartResponse)
        ));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCart(@PathVariable Long id) {
        log.info("Deleting cart with ID: {}", id);
        return fakeStoreService.deleteCart(id)
                .thenReturn(ResponseEntity.noContent().<Void>build())
                .doOnSuccess(response -> log.info("Deleted cart with ID: {}", id))
                .onErrorResume(e -> {
                    log.error("Error deleting cart with ID: {}", id, e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

//    @PatchMapping("/{id}/products")
//    public Mono<ResponseEntity<CartsResponseDTO>> addProductToCart(
//            @PathVariable Long id,
//            @RequestBody CartProductDTO productDto) {
//        log.info("Adding product to cart ID: {}", id);
//        return fakeStoreService.addProductToCart(id, cartsMapper.mapProductToFakeStore(productDto))
//                .map(cartsMapper::fakeStoreToCartResponse)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
    @DeleteMapping("/{id}/products/{productId}")
    public Mono<ResponseEntity<CartsResponseDTO>> removeProductFromCart(
            @PathVariable Long id,
            @PathVariable Long productId) {
        log.info("Removing product {} from cart ID: {}", productId, id);
        return fakeStoreService.removeProductFromCart(id, productId)
                .map(cartsMapper::fakeStoreToCartResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
