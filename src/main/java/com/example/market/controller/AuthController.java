package com.example.market.controller;

import com.example.market.dto.response.JwtAuthenticationResponse;
import com.example.market.dto.request.SignInRequest;
import com.example.market.dto.request.SignUpRequest;
import com.example.market.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/sign-up")
    @Operation(summary = "User registration")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request,
                                            @RequestHeader(name = "Accept-Language") Locale locale) {
        return authenticationServiceImpl.signUp(request, locale);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "User authorization")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request,
                                            @RequestHeader(name = "Accept-Language") Locale locale) {
        return authenticationServiceImpl.signIn(request, locale);
    }
}