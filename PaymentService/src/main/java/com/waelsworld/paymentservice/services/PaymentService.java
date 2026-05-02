package com.waelsworld.paymentservice.services;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.waelsworld.paymentservice.controllers.PaymentController;
import com.waelsworld.paymentservice.dtos.InitiatePaymentRequestDto;
import com.waelsworld.paymentservice.paymentgateways.AvailablePaymentGateway;
import com.waelsworld.paymentservice.paymentgateways.PaymentGateway;
import com.waelsworld.paymentservice.paymentgateways.PaymentGatewayStrategySelector;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    PaymentGatewayStrategySelector paymentGatewayStrategySelector;
    public PaymentService(PaymentGatewayStrategySelector paymentGatewayStrategySelector) {
        this.paymentGatewayStrategySelector = paymentGatewayStrategySelector;
    }
    public String initiatePayment(InitiatePaymentRequestDto request) throws StripeException, RazorpayException {
        PaymentGateway paymentGateway = paymentGatewayStrategySelector.getPaymentGateway(AvailablePaymentGateway.STRIPE);
        return paymentGateway.generatePaymentUrl(request);
    }
}
