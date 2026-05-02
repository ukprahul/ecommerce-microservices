package com.ecommerce.cartservice.controllers;

import com.ecommerce.cartservice.dtos.AddToCartRequestDto;
import com.ecommerce.cartservice.dtos.CartResponseDto;
import com.ecommerce.cartservice.models.CartItem;
import com.ecommerce.cartservice.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartItem> addToCart(@PathVariable Long userId,
                                               @RequestBody AddToCartRequestDto dto) {
        return ResponseEntity.ok(cartService.addToCart(userId, dto));
    }

    @DeleteMapping("/{userId}/item/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId,
                                                @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
