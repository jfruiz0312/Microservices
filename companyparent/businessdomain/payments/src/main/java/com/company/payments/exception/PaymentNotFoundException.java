/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.payments.exception;

/**
 *
 * @author ruiz_
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException() {
        super("Payment not found");
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentNotFoundException(Throwable cause) {
        super("Payment not found", cause);
    }

    public PaymentNotFoundException(String paymentId, String additionalInfo) {
        super(String.format("Payment  ID %s not found. %s", paymentId, additionalInfo));
    }
}