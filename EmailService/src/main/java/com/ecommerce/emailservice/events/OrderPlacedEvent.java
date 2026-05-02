package com.ecommerce.emailservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {

    private Long orderId;
    private Long userId;
    private String customerEmail;
    private Double totalAmount;
    private LocalDateTime placedAt;
}
