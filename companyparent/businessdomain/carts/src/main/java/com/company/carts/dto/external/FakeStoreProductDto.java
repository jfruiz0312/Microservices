
package com.company.carts.dto.external;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ruiz_
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FakeStoreProductDto {

   private Long id;
    private String title;
    private BigDecimal price;
    private Integer quantity; // 
}
