package com.example.market.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JWTTokenProvider jwtTokenProvider;

    public SecurityConfiguration(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(auth -> auth
                        .antMatchers("/book/create",
                                "/book/updateById/*",
                                "/book/deleteById/*").hasAuthority("ADMIN")
                        .antMatchers("/user", "/user/login").permitAll()
                        .antMatchers(HttpMethod.GET, "/v2/api-docs",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/webjars/**",
                                "/*.html",
                                "/**/*.html",
                                "/**/*.css",
                                "/**/*.js").permitAll()
                        .antMatchers("/db/**").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JWTTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}