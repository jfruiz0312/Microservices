
package com.company.payments.repository;

import com.company.payments.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ruiz_
 */
@Repository
public interface OrderRepository extends  JpaRepository<Order, Long>{
    
}
