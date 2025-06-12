/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ruiz_
 */
@Data
public class Cart {
private long id;
    private long userId;
    private String date;  // or LocalDateTime if parsed
    private List<CartProduct> products;
    @JsonProperty("__v")
    private int version;

    @Data
    public static class CartProduct {
        @JsonProperty("productId")
        private long productId;
        private int quantity;
    }
}