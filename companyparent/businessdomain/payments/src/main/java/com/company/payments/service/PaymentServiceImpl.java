package com.company.payments.service;

import com.company.payments.dto.PaymentRequestDto;
import com.company.payments.dto.PaymentResponseDto;
import com.company.payments.entities.Payment;
import com.company.payments.enums.PaymentMethod;
import com.company.payments.enums.PaymentStatus;
import com.company.payments.exception.PaymentNotFoundException;
import com.company.payments.mapper.PaymentMapper;
import com.company.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 *
 * @author ruiz_
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequest) {
        // Use mapper to convert DTO to entity
        Payment payment = paymentMapper.toEntity(paymentRequest);
        
        // Set additional fields that aren't in the DTO
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        
       
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponseDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toResponseDto(payment);
    }

    // Helper method to generate transaction ID
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}