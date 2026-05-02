package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.CreateOrderRequestDto;
import com.ecommerce.orderservice.dtos.OrderItemDto;
import com.ecommerce.orderservice.events.OrderPlacedEvent;
import com.ecommerce.orderservice.kafka.OrderEventProducer;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.models.OrderItem;
import com.ecommerce.orderservice.models.OrderStatus;
import com.ecommerce.orderservice.repositories.OrderItemRepository;
import com.ecommerce.orderservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public Order createOrder(CreateOrderRequestDto dto) {
        double totalAmount = dto.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .userId(dto.getUserId())
                .customerEmail(dto.getCustomerEmail())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(totalAmount)
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto itemDto : dto.getItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .productId(itemDto.getProductId())
                    .productName(itemDto.getProductName())
                    .price(itemDto.getPrice())
                    .quantity(itemDto.getQuantity())
                    .imageUrl(itemDto.getImageUrl())
                    .build();
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getCustomerEmail(),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );
        orderEventProducer.publishOrderPlaced(event);

        return savedOrder;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
