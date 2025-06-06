
package com.company.payments.dto;

import com.company.payments.enums.PaymentMethod;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class PaymentRequestDto {
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private String description;

}
