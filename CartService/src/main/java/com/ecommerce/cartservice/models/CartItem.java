package com.ecommerce.cartservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Long userId;
    private Long productId;
    private String productName;
    private Double price;
    private int quantity;
    private String imageUrl;
}
