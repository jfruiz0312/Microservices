package com.company.payments.mapper;

import com.company.payments.dto.PaymentRequestDto;
import com.company.payments.dto.PaymentResponseDto;
import com.company.payments.entities.Payment;
import com.company.payments.enums.PaymentStatus;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
/**
 *
 * @author ruiz_
 */


@Component
public class PaymentMapper {

    private final ObjectMapper objectMapper;

    public PaymentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Convert PaymentRequestDto to Payment entity
    public Payment toEntity(PaymentRequestDto paymentRequestDto) {
        Payment payment = objectMapper.convertValue(paymentRequestDto, Payment.class);
        payment.setStatus(PaymentStatus.PENDING); // Default status
        payment.setPaymentDate(LocalDateTime.now()); // Set current time
        payment.setTransactionId(generateTransactionId()); // Generate transaction ID
        return payment;
    }

    // Convert Payment entity to PaymentResponseDto
    public PaymentResponseDto toResponseDto(Payment payment) {
        PaymentResponseDto responseDto = objectMapper.convertValue(payment, PaymentResponseDto.class);
        responseDto.setStatus(payment.getStatus().toString());
        return responseDto;
    }

    // Convert Payment entity to PaymentRequestDto (if needed)
    public PaymentRequestDto toRequestDto(Payment payment) {
        return objectMapper.convertValue(payment, PaymentRequestDto.class);
    }

    // Helper method to generate transaction ID
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }

}
