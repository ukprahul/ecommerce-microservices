package com.waelsworld.paymentservice.paymentgateways;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.waelsworld.paymentservice.dtos.InitiatePaymentRequestDto;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentGateway implements PaymentGateway {
    private final RazorpayClient razorpayClient;
    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }
    @Override
    public String generatePaymentUrl(InitiatePaymentRequestDto paymentRequestDto) throws RazorpayException {
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",paymentRequestDto.getUnitAmount()*paymentRequestDto.getQuantity());
        paymentLinkRequest.put("currency",paymentRequestDto.getCurrency());
        paymentLinkRequest.put("accept_partial",false);
        paymentLinkRequest.put("expire_by", 1701624719);
        paymentLinkRequest.put("reference_id",paymentRequestDto.getOrderId());
        paymentLinkRequest.put("description","Payment for order #" + paymentRequestDto.getOrderId());
        JSONObject customer = new JSONObject();
        customer.put("name",paymentRequestDto.getName());
        customer.put("contact",paymentRequestDto.getContact());
        customer.put("email",paymentRequestDto.getEmail());
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        paymentLinkRequest.put("callback_url","https://example-callback-url.com/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
        return payment.get("short_url").toString();
    }
}
