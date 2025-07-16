package com.example.market.service;

import com.example.market.dto.request.SignInRequest;
import com.example.market.dto.request.SignUpRequest;
import com.example.market.dto.response.JwtAuthenticationResponse;

import java.util.Locale;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request, Locale locale);
    JwtAuthenticationResponse signIn(SignInRequest request, Locale locale);
}
