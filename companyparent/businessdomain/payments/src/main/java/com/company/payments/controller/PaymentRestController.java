/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.company.payments.controller;

import com.company.payments.dto.Cart;
import com.company.payments.dto.PaymentDto;
import com.company.payments.dto.ProductDto;
import com.company.payments.entities.Order;
import com.company.payments.entities.OrderDetail;
import com.company.payments.entities.Payment;
import com.company.payments.enums.PaymentMethod;
import com.company.payments.enums.PaymentStatus;
import com.company.payments.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author ruiz_
 */
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    private final WebClient.Builder webClientBuilder;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PaymentRestController(WebClient.Builder webClientBuilder, PaymentService paymentService) {
        this.webClientBuilder = webClientBuilder;
        this.paymentService = paymentService;
    }

    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain 
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    @GetMapping("/{id}")
    public ResponseEntity<Object> payment(@PathVariable("id") String id) {
        log.info("create-payment-by-id, {} ", id);
        Long cartId = Long.valueOf(id);

        try {
            // 1. Obtener el carrito
            String cartJson = findProductInCart(cartId);
            Cart cart = objectMapper.readValue(cartJson, Cart.class);
            log.info("cartJson {} ", cartJson);

            // 2. Crear la orden principal
            Order order = new Order();
            order.setUserId(cart.getUserId());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("PENDING_PAYMENT");

            order = paymentService.createOrder(order);

            double orderTotal = 0.0;

            // 3. Procesar cada producto del carrito
            for (Cart.CartProduct cartProduct : cart.getProducts()) {
                // Obtener detalles del producto
                String productJson = getPriceProduct(cartProduct.getProductId());
                log.info("productJson {} ", productJson);
                ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
//                List<ProductDto> products = objectMapper.readValue(productJson, new TypeReference<List<ProductDto>>() {
//                }
//                );

                // Calcular subtotal
                double subtotal = productDto.getPrice() * cartProduct.getQuantity();

                // Crear detalle de orden
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProductId(productDto.getId());
                orderDetail.setProductName(productDto.getTitle());
                orderDetail.setQuantity(cartProduct.getQuantity());
                orderDetail.setUnitPrice(productDto.getPrice());
                orderDetail.setSubtotal(subtotal);

                paymentService.createOrderDetail(orderDetail);
                // Acumular total
                orderTotal += subtotal;
            }

            // 4. Actualizar el total de la orden
            order.setTotal(orderTotal);
            paymentService.createOrder(order);

            return ResponseEntity.ok(Map.of(
                    "message", "Order created successfully",
                    "orderId", order.getId(),
                    "total", orderTotal
            ));

        } catch (Exception ex) {
            log.error("Error creating order: ", ex);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Failed to create order",
                    "message", ex.getMessage()
            ));
        }
    }

    private String findProductInCart(long id) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("https://fakestoreapi.com/carts")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "https://fakestoreapi.com/carts"))
                .build();

        JsonNode json = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();

        // Eliminar el campo version si existe
        if (json.isObject()) {
            ObjectNode objectNode = (ObjectNode) json;
            objectNode.remove("version");
        }
        log.info("block {} ", json);
        // String name = block.get("name").asText();
        return json.toString();
    }

    private String getPriceProduct(long id) throws JsonProcessingException {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("https://fakestoreapi.com/products")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "https://fakestoreapi.com/products"))
                .build();

        JsonNode json = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();

        // Crear un nuevo objeto JSON con solo los campos requeridos
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        if (json != null && json.isObject()) {
            result.put("id", json.path("id").asLong());
            result.put("title", json.path("title").asText());
            result.put("price", json.path("price").asDouble());
            result.put("description", json.path("description").asText());
        }

        return result.toString();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") String id) {
        Order response = null;
        Long idOrder = Long.valueOf(id);
        Optional<Order> orderOpcional = paymentService.getOderById(idOrder);
        if (orderOpcional.isPresent()) {
            response = orderOpcional.get();
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "Error", " The order with "+ id +" was not found"));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<Object> putOderPayment(@RequestBody PaymentDto dto) {
        Order orderUpdate = new Order();
        Payment paymentResponse=null;

        log.info("id" + dto.getId());
        log.info("paymentDto {} ", dto);
        Optional<Order> orderOpcional = paymentService.getOderById(dto.getId());
        if (orderOpcional.isPresent() && orderOpcional.get().getTotal().equals(dto.getTotal())) {
            orderUpdate.setTotal(dto.getTotal());
            orderUpdate.setStatus(PaymentStatus.COMPLETED.toString());
            orderUpdate.setOrderDate(LocalDateTime.now());
            Order orderExist = paymentService.updateOrder(dto.getId(), orderUpdate);
            if (orderExist != null) {
                Payment paymentUpdate = new Payment();
                paymentUpdate.setAmount(orderExist.getTotal());
                paymentUpdate.setOrderId(orderExist.getId());
                PaymentMethod method = PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase());
                paymentUpdate.setPaymentMethod(method);
                paymentUpdate.setStatus(PaymentStatus.COMPLETED);
                paymentUpdate.setPaymentDate(LocalDateTime.now());
                paymentUpdate.setTransactionId(UUID.randomUUID().toString().replace("-", ""));
                paymentUpdate.setDescription("payment order the userID, " +orderExist.getUserId());
                paymentUpdate.setCurrency(dto.getCurrency());
                paymentResponse= paymentService.savePayment(paymentUpdate);

            }

        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "Error", " The Payment with " + dto.getId() + " was not found"));
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}
