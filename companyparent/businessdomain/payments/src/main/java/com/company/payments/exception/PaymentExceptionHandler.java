
package com.company.payments.exception;
import com.company.payments.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author ruiz_
 */
@RestControllerAdvice
public class PaymentExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentExceptionHandler.class);

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(
            PaymentNotFoundException ex, 
            WebRequest request) {
        
        logger.error("Payment not found: {} - Path: {}", ex.getMessage(), request.getDescription(false));
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Payment not found",
            "Payment not found no exits",
            System.currentTimeMillis()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}