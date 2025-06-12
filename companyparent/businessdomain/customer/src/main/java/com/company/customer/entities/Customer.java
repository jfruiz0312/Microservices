package com.company.customer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
@Entity
public class Customer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 1, max = 100, message = "Name length must be between 1 and 100 characters")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name can only contain letters and spaces")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Address cannot be empty")
    @Size(min = 1, max = 100, message = "Address length must be between 1 and 100 characters")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Phone number cannot be empty")
    @Size(min = 9, max = 9, message = "Phone number must be exactly 9 digits")
    @Pattern(regexp = "^\\d{9}$", message = "Phone number must contain only digits")
    @Column(nullable = false)
    private String phone;
}
