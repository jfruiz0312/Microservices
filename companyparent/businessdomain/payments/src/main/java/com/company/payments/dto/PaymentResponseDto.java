
package com.company.payments.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ruiz_
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
     private String status;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String message;
}
