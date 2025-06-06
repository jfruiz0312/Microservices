
package com.company.payments.repository;

import com.company.payments.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ruiz_
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
