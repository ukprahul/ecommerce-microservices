package com.ecommerce.orderservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    private Long userId;
    private String customerEmail;
    private List<OrderItemDto> items;
}
