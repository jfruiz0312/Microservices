
package com.company.payments.repository;

import com.company.payments.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ruiz_
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
