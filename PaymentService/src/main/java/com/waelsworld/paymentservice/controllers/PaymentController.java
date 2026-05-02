package com.waelsworld.paymentservice.controllers;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.waelsworld.paymentservice.dtos.InitiatePaymentRequestDto;
import com.waelsworld.paymentservice.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public ResponseEntity<String> initiatePayment(@RequestBody InitiatePaymentRequestDto request) {
        // instead of dto request only order id should be passed,
        // and the order details should be fetched from the db
        // by calling the order service or productservice in this case
        // -> currently we are returning the payment url directly, it should
        // directly redirect to the url to complete the payment
        // -> dto should be used based on the payment gateway, when fetching the order details
        try {
            return ResponseEntity.ok(paymentService.initiatePayment(request));
        } catch (StripeException | RazorpayException e) {
            //PaymentserviceApplication.logger.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
