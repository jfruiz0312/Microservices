
package com.company.product.dto.external;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class FakeStoreProductDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private FakeStoreRatingDto rating;
}

@Data
class FakeStoreRatingDto {

    private BigDecimal rate;
    private Integer count;
}
