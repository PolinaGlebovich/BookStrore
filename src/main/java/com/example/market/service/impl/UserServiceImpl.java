package com.example.market.service.impl;

import com.example.market.entity.Cart;
import com.example.market.entity.Role;
import com.example.market.entity.User;
import com.example.market.exception.UserNotFoundException;
import com.example.market.repository.UserRepository;
import com.example.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        user.setRole(Set.of(Role.ADMIN));
        userRepository.save(user);
        log.info("Save new user {} with cart {}", user, cart);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findByUsername = userRepository.findUserByUsername(username);
        User user = findByUsername.orElseThrow(UserNotFoundException::new);

        return new CustomUserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList())
        );
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("Delete user");
    }

    public Long getUserId(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}