package com.company.orders.service;

import com.company.orders.dto.OrderDto;
import com.company.orders.entities.OrderDetalle;
import com.company.orders.mapper.OrderMapper;
import com.company.orders.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ruiz_
 */
@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderDetalle saveOrderFromJson(String json) throws Exception {
        log.info("OrderService saveOrderFromJson {} " , json);
        OrderDetalle order = orderMapper.mapAndPrepareForSave(json);
       
        return orderRepository.save(order);
    }

    @Transactional
    public OrderDto getOrderById(Long id) {
        OrderDetalle order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.mapOrderToDto(order);
    }

    @Transactional
    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::mapOrderToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto updateOrderStatus(Long id, String status) {
        OrderDetalle order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        OrderDetalle updatedOrder = orderRepository.save(order);
        return orderMapper.mapOrderToDto(updatedOrder);
    }
}
