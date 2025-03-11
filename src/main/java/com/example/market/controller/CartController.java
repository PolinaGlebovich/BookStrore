package com.example.market.controller;

import com.example.market.entity.Cart;
import com.example.market.service.CartService;
import com.example.market.service.impl.CustomUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addBookToCart(
            @RequestParam("bookId") Long bookId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Long userId = ((CustomUserDetailsImpl) userDetails).getId();

            cartService.addBookToCart(userId, bookId);
            return ResponseEntity.ok("Book added to cart successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findCartById/{id}")
    public ResponseEntity<Optional<Cart>> findCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartService.findById(id);
        return ResponseEntity.ok(cart);
    }

}
