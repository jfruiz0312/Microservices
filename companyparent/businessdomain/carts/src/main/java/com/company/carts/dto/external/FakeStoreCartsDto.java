package com.company.carts.dto.external;

import java.util.List;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class FakeStoreCartsDto {

    private Long id;
    private Long userId;
    private String date;
    private List<FakeStoreCartProductDto> products;
    private Integer __v;
}
