
package com.company.payments.dto;

import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class PaymentDto {

    private Long id;
    private Double total;// 798.04
    private String paymentMethod;
    private String currency;//(ej: USD, EUR, MXN)
}
