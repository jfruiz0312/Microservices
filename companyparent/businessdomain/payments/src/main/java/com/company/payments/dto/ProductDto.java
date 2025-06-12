package com.company.payments.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class ProductDto implements Serializable{

    private Long id;
    private String title;
    private Double price;
    private String description;
}