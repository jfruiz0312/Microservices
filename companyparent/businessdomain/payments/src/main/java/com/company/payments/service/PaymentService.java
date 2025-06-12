package com.company.payments.service;

import com.company.payments.entities.OrderDetail;
import com.company.payments.entities.Order;
import com.company.payments.entities.Payment;
import com.company.payments.exception.PaymentNotFoundException;
import com.company.payments.repository.OrderDetailRepository;
import com.company.payments.repository.OrderRepository;
import com.company.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ruiz_
 */
@Service
@Transactional
public class PaymentService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Autowired

    public PaymentService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {

        return orderDetailRepository.save(orderDetail); // Aquí usamos el save
    }

    @Transactional
    public Order createOrder(Order order) {

        return orderRepository.save(order); // Aquí usamos el save
    }

    public Optional<Order> getOderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Order not found with id: " + id));

        // Update only the fields that should be modifiable
        if (orderDetails.getUserId() != null) {
            existingOrder.setUserId(orderDetails.getUserId());
        }
        if (orderDetails.getStatus() != null) {
            existingOrder.setStatus(orderDetails.getStatus());
        }
        // Continue with other fields...

        return orderRepository.save(existingOrder);
    }

    @Transactional
    public Payment savePayment(Payment payment) {

        return paymentRepository.save(payment); // Aquí usamos el save
    }
}
