
package com.company.product.dto;

import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class ProductRequestDTO {
  private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
}