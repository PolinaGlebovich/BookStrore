package com.example.market.service.impl;

import com.example.market.dto.response.CartResponse;
import com.example.market.entity.Book;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;
import com.example.market.exception.BookNotFoundException;
import com.example.market.exception.CartItemNotFoundException;
import com.example.market.exception.CartNotFoundException;
import com.example.market.mapper.CartMapper;
import com.example.market.repository.BookRepository;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.CartRepository;
import com.example.market.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = "cartCache")
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final MessageSource messageSource;

    @Override
    @CachePut(value = "carts", key = "#userId")
    @Transactional
    public CartResponse addBookToCart(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("cart.book.not_found", bookId));

        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new CartNotFoundException("cart.not_found", userId));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCart(cart);

        cartItemRepository.save(cartItem);

        log.info(messageSource.getMessage("cart.book.added", new Object[]{bookId, userId}, LocaleContextHolder.getLocale()));

        CartResponse cartResponse = cartMapper.toDto(cart);
        cartResponse.setItems(cartMapper.mapCartItems(cart.getCartItems()));
        return cartResponse;
    }

    @Override
    @Cacheable(value = "carts", key = "#id", unless = "#result == null")
    public Optional<CartResponse> findById(Long id) {
        return cartRepository.findById(id)
                .map(cart -> {
                    CartResponse cartResponse = cartMapper.toDto(cart);
                    cartResponse.setItems(cartMapper.mapCartItems(cart.getCartItems()));
                    return cartResponse;
                });
    }

    @Override
    @Transactional
    @CacheEvict(value = "carts", key = "#userId")
    public void deleteBookFromCart(Long userId, Long bookId) {
        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new CartNotFoundException("cart.not_found", userId));

        CartItem cartItem = cartItemRepository.findByBookIdAndCartId(bookId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException("cart.item.not_found", bookId, userId));

        cartItemRepository.delete(cartItem);

        log.info(messageSource.getMessage("cart.book.removed", new Object[]{bookId, userId}, LocaleContextHolder.getLocale()));
    }
}