package com.example.market.service.impl;

import com.example.market.dto.request.UserRequest;
import com.example.market.dto.response.UserResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.User;
import com.example.market.exception.UserAlreadyExistsException;
import com.example.market.exception.UserNotFoundException;
import com.example.market.mapper.UserMapper;
import com.example.market.repository.UserRepository;
import com.example.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Slf4j
@CacheConfig(cacheNames = "userCache")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public UserResponse save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(
                    messageSource.getMessage("user.already_exists", null, LocaleContextHolder.getLocale()));
        }

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        userRepository.save(user);
        log.info(messageSource.getMessage("user.saved", null, LocaleContextHolder.getLocale()));
        return userMapper.toDto(user);
    }

    @Override
    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("user.id_not_found", new Object[]{id}, LocaleContextHolder.getLocale())));
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @CachePut(value = "users", key = "#user.id")
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("user.not_found", null, LocaleContextHolder.getLocale())));

        userMapper.updateUserFromRequest(userRequest, user);

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        if (userRequest.getRoles() != null) {
            user.setRole(userRequest.getRoles());
        }

        User updatedUser = userRepository.save(user);
        log.info(messageSource.getMessage("user.updated", null, LocaleContextHolder.getLocale()));
        return userMapper.toDto(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    messageSource.getMessage("user.id_not_found", new Object[]{id}, LocaleContextHolder.getLocale()));
        }
        userRepository.deleteById(id);
        log.info(messageSource.getMessage("user.deleted", null, LocaleContextHolder.getLocale()));
    }

    @Override
    public UserResponse getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("user.username_not_found", new Object[]{username}, LocaleContextHolder.getLocale())));
    }

    @Override
    @Cacheable(cacheNames = "users")
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
}
