package com.waelsworld.paymentservice.paymentgateways;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.waelsworld.paymentservice.dtos.InitiatePaymentRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public String generatePaymentUrl(InitiatePaymentRequestDto requestDto) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:4242/success")
                        .setCancelUrl("http://localhost:4242/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(requestDto.getQuantity())
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(requestDto.getCurrency())
                                                        .setUnitAmount(requestDto.getUnitAmount())
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(requestDto.getName())
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();

        Session session = Session.create(params);
        //response.redirect(session.getUrl(), 303);

        return session.getUrl();
    }
}
