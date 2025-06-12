
package com.company.carts.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
@Valid
public class CartsRequestDTO {

    @NotBlank
    private Long userId;
    @NotNull
    private String date;
    private List<CartProductDTO> products;
}
