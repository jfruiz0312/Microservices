
package com.company.carts.dto;


import java.util.List;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class CartsResponseDTO {

    private Long id;
    private Long userId;
    private String date;
    private List<CartProductDTO> products;
    private Integer version;
}
