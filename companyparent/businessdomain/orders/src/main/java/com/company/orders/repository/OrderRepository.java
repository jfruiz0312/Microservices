
package com.company.orders.repository;

import com.company.orders.entities.OrderDetalle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ruiz_
 */
public interface OrderRepository extends JpaRepository<OrderDetalle, Long>{
        List<OrderDetalle> findByUserId(Long userId);
}
