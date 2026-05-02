package com.ecommerce.cartservice.dtos;

import com.ecommerce.cartservice.models.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private Long userId;
    private List<CartItem> items;
    private Double totalAmount;
}
