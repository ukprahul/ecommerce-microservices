package com.ecommerce.emailservice.kafka;

import com.ecommerce.emailservice.events.OrderPlacedEvent;
import com.ecommerce.emailservice.services.EmailNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final EmailNotificationService emailNotificationService;

    public OrderEventConsumer(ObjectMapper objectMapper,
                               EmailNotificationService emailNotificationService) {
        this.objectMapper = objectMapper;
        this.emailNotificationService = emailNotificationService;
    }

    @KafkaListener(topics = "order.placed", groupId = "email-service")
    public void handleOrderPlaced(String message) {
        try {
            logger.info("Received order placed event: {}", message);
            OrderPlacedEvent event = objectMapper.readValue(message, OrderPlacedEvent.class);
            logger.info("Processing order confirmation for orderId: {}, customerEmail: {}",
                    event.getOrderId(), event.getCustomerEmail());
            emailNotificationService.sendOrderConfirmationEmail(event);
        } catch (Exception e) {
            logger.error("Failed to process order placed event. Message: {}. Error: {}",
                    message, e.getMessage(), e);
        }
    }
}
