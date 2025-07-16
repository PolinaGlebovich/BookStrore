package com.example.market.controller;

import com.example.market.dto.response.CartResponse;
import com.example.market.exception.CartNotFoundException;
import com.example.market.exception.ResourceNotFoundException;
import com.example.market.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "APIs for managing user carts")
public class CartController {

    private final CartService cartService;
    private final MessageSource messageSource;

    @PostMapping("/{userId}/add/{bookId}")
    @Operation(summary = "Add a book to the cart", description = "Add a book to the user's cart by user ID and book ID")
    public ResponseEntity<?> addBookToCart(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        CartResponse cartResponse = cartService.addBookToCart(userId, bookId);
        String message = messageSource.getMessage("cart.book.added", new Object[]{bookId, userId}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", cartResponse));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cart by ID", description = "Retrieve a cart by its unique ID")
    public ResponseEntity<?> getCartById(
            @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        CartResponse cartResponse = cartService.findById(id)
                .orElseThrow(() -> new CartNotFoundException("cart.not_found", id));
        String message = messageSource.getMessage("cart.found", new Object[]{id}, locale);
        return ResponseEntity.ok(Map.of("message", message, "data", cartResponse));
    }

    @DeleteMapping("/{userId}/remove/{bookId}")
    @Operation(summary = "Remove a book from the cart", description = "Remove a book from the user's cart by user ID and book ID")
    public ResponseEntity<?> deleteBookFromCart(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        cartService.deleteBookFromCart(userId, bookId);
        String message = messageSource.getMessage("cart.book.removed", new Object[]{bookId, userId}, locale);
        return ResponseEntity.ok(Map.of("message", message));
    }
}