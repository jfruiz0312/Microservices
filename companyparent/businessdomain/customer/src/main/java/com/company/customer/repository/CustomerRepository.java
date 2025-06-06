
package com.company.customer.repository;

import com.company.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ruiz_
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
