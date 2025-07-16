package com.example.market.service;

import com.example.market.dto.response.CartResponse;
import com.example.market.entity.Cart;

import java.util.Optional;

public interface CartService {
    CartResponse addBookToCart(Long userId, Long bookId);
    Optional<CartResponse> findById(Long id);
    void deleteBookFromCart(Long userId, Long bookId);
}
