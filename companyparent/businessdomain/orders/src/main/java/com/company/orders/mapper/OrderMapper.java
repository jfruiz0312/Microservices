package com.company.orders.mapper;

import com.company.orders.dto.OrderDto;
import com.company.orders.dto.ProductItemDto;
import com.company.orders.entities.OrderDetalle;
import com.company.orders.entities.ProductItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author ruiz_
 */
@Slf4j
@Component
public class OrderMapper {

    private final ObjectMapper objectMapper;

    public OrderMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public OrderDetalle mapJsonToOrder(String json) throws Exception {
        return objectMapper.readValue(json, OrderDetalle.class);
    }

    public OrderDetalle mapAndPrepareForSave(String json) throws Exception {
        // Deserializar JSON a DTO
        OrderDto orderDto = objectMapper.readValue(json, OrderDto.class);
        log.info("Order preparada para guardar: {}", orderDto);
        // Validar y mapear a entidad Order
        OrderDetalle order = mapDtoToEntity(orderDto);

        return order;
    }

    private OrderDetalle mapDtoToEntity(OrderDto dto) {
        OrderDetalle order = new OrderDetalle();

        // Mapear campos básicos
        order.setUserId(dto.getUserId());
        order.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");

        // Validaciones
        if (order.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (dto.getProducts() == null || dto.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        // Mapear productos
        for (ProductItemDto itemDto : dto.getProducts()) {
            if (itemDto.getProductId() == null) {
                throw new IllegalArgumentException("Product ID is required");
            }
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for product ID: " + itemDto.getProductId());
            }

            ProductItem item = new ProductItem();
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            item.setOrderDetalle(order);
            order.addProductItem(item);
        }

        return order;
    }

    public OrderDto mapOrderToDto(OrderDetalle order) {
        log.info("mapOrderToDto");
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setDate(order.getDate());
        dto.setStatus(order.getStatus());

        List<ProductItemDto> productDtos = order.getProducts().stream()
                .map(this::mapProductItemToDto)
                .collect(Collectors.toList());

        dto.setProducts(productDtos);
        return dto;
    }

    private ProductItemDto mapProductItemToDto(ProductItem productItem) {
        ProductItemDto dto = new ProductItemDto();
        dto.setProductId(productItem.getProductId());
        dto.setQuantity(productItem.getQuantity());
        return dto;
    }
}
