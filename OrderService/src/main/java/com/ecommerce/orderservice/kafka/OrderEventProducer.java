package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.events.OrderPlacedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "order.placed";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishOrderPlaced(OrderPlacedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, message);
            logger.info("Successfully published OrderPlacedEvent for orderId: {}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Failed to publish OrderPlacedEvent for orderId: {}. Error: {}",
                    event.getOrderId(), e.getMessage(), e);
        }
    }
}
