
package com.company.payments.repository;

import com.company.payments.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ruiz_
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
    
}
