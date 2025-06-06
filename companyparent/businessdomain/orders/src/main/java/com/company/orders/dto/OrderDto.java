/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author ruiz_
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime date;
    private String status;
    private List<ProductItemDto> products;
}
