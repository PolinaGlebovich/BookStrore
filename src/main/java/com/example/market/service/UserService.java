package com.example.market.service;


import com.example.market.dto.request.SignUpRequest;
import com.example.market.dto.request.UserRequest;
import com.example.market.dto.response.UserResponse;
import com.example.market.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse findUserById(Long id);
    Page<UserResponse> findAll(Pageable pageable);
    UserResponse save(User user);
    void delete(Long id);
    UserResponse getCurrentUser();
    UserResponse updateUser(Long id, UserRequest userRequest);
    Optional<User> findByUsername(String username);
}
