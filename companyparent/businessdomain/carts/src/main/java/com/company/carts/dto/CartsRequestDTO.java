
package com.company.carts.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class CartsRequestDTO {

    private Long userId;
    private String date;
    private List<CartProductDTO> products;
}
