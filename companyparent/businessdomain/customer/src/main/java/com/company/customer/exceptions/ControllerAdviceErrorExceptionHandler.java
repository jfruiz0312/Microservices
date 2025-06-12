package com.company.customer.exceptions;

import jakarta.validation.ConstraintViolationException;
import com.company.customer.records.Error;
import com.company.customer.records.ErrorDetail;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 *
 * @author ruiz_
 */
@ControllerAdvice
public class ControllerAdviceErrorExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorDetail> details
                = ex.getConstraintViolations().stream()
                        .map(e -> new ErrorDetail(e.getPropertyPath().toString().replace("customer", ""), e.getMessage()))
                        .toList();
        return new ResponseEntity<>(new Error("Validation failed for argument", details), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> new ErrorDetail(e.getField(), e.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new Error("Data validation error...", details), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<ErrorDetail> details = new ArrayList<>();
        details.add(new ErrorDetail("", ex.toString()));

        return new ResponseEntity<>(new Error("Data integrity violation error...", details), BAD_REQUEST);
    }
}
