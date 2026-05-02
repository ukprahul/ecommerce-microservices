package com.ecommerce.emailservice.services;

import com.ecommerce.emailservice.events.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmationEmail(OrderPlacedEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getCustomerEmail());
            message.setSubject("Order Confirmation #" + event.getOrderId());
            message.setText(
                    "Dear Customer,\n\n" +
                    "Thank you for your order! Your order has been placed successfully.\n\n" +
                    "Order Details:\n" +
                    "Order ID: " + event.getOrderId() + "\n" +
                    "Total Amount: $" + event.getTotalAmount() + "\n" +
                    "Placed At: " + event.getPlacedAt() + "\n\n" +
                    "We will notify you when your order is shipped.\n\n" +
                    "Best regards,\nThe Ecommerce Team"
            );
            mailSender.send(message);
            logger.info("Order confirmation email sent successfully to: {} for orderId: {}",
                    event.getCustomerEmail(), event.getOrderId());
        } catch (MailException e) {
            logger.warn("Failed to send order confirmation email to: {} for orderId: {}. Error: {}",
                    event.getCustomerEmail(), event.getOrderId(), e.getMessage());
        } catch (Exception e) {
            logger.warn("Unexpected error while sending email for orderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage());
        }
    }
}
