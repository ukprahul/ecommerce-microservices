package com.waelsworld.paymentservice.paymentgateways;

import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayStrategySelector {
    private final RazorpayPaymentGateway razorpayPaymentGateway;
    private final StripePaymentGateway stripePaymentGateway;

    public PaymentGatewayStrategySelector(RazorpayPaymentGateway razorpayPaymentGateway,
                                          StripePaymentGateway stripePaymentGateway) {
        this.razorpayPaymentGateway = razorpayPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public PaymentGateway getPaymentGateway(AvailablePaymentGateway selectedPaymentGateway) {
        // there can be various parameters to select the payment gateway
        // like number of successful transactions, amount of transaction, etc.
        // for now, we are just using a string
        if (selectedPaymentGateway.equals(AvailablePaymentGateway.RAZORPAY)) {
            return razorpayPaymentGateway;
        } else if (selectedPaymentGateway.equals(AvailablePaymentGateway.STRIPE)) {
            return stripePaymentGateway;
        } else {
            throw new IllegalArgumentException("Invalid payment gateway");
        }
    }
}
