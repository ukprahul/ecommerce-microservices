package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.dtos.AddToCartRequestDto;
import com.ecommerce.cartservice.dtos.CartResponseDto;
import com.ecommerce.cartservice.models.CartItem;
import com.ecommerce.cartservice.repositories.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartResponseDto getCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        return new CartResponseDto(userId, items, totalAmount);
    }

    @Transactional
    public CartItem addToCart(Long userId, AddToCartRequestDto dto) {
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, dto.getProductId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + dto.getQuantity());
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .userId(userId)
                    .productId(dto.getProductId())
                    .productName(dto.getProductName())
                    .price(dto.getPrice())
                    .quantity(dto.getQuantity())
                    .imageUrl(dto.getImageUrl())
                    .build();
            return cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
