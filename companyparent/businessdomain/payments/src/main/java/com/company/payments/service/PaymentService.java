
package com.company.payments.service;

import com.company.payments.dto.PaymentRequestDto;
import com.company.payments.dto.PaymentResponseDto;

/**
 *
 * @author ruiz_
 */
public interface PaymentService {

    PaymentResponseDto createPayment(PaymentRequestDto paymentRequest);

    PaymentResponseDto getPaymentById(Long id);
}
