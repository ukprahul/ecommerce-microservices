package com.waelsworld.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentRequestDto {
    Long quantity;
    String currency;
    Long unitAmount;
    String name;
    String orderId;
    String contact;
    String email;
}
