package com.example.market.service;


import com.example.market.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User findUserById(Long Id);
    List<User> findAll();
    User save (User user);
    void delete(Long id);
    UserDetails loadUserByUsername(String username);
}
