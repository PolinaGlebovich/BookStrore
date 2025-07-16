package com.example.market.service.impl;

import com.example.market.configuration.JwtService;
import com.example.market.dto.request.SignInRequest;
import com.example.market.dto.response.JwtAuthenticationResponse;
import com.example.market.dto.request.SignUpRequest;
import com.example.market.entity.Role;
import com.example.market.entity.User;
import com.example.market.service.AuthenticationService;
import com.example.market.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MessageSource messageSource;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request, Locale locale) {
        var user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Set.of(Role.ADMIN))
                .build();

        userService.save(user);

        var token = jwtService.generateToken(user);

        String message = messageSource.getMessage("registration.success", null, locale);

        log.info("User {} registered", user.getUsername());

        return JwtAuthenticationResponse.builder()
                .token(token)
                .message(message)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request, Locale locale) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            String errorMessage = messageSource.getMessage("login.error", null, locale);

            log.error("Login failed for user: {}", request.getUsername());

            return JwtAuthenticationResponse.builder()
                    .message(errorMessage)
                    .build();
        }

        var user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("user.username_not_found", new Object[]{request.getUsername()}, locale)));

        var token = jwtService.generateToken(user);

        String message = messageSource.getMessage("login.success", null, locale);

        log.info("User {} is authorized", request.getUsername());

        return JwtAuthenticationResponse.builder()
                .token(token)
                .message(message)
                .build();
    }
}