package com.example.market.controller;

import com.example.market.dto.request.UserRequest;
import com.example.market.dto.response.UserResponse;
import com.example.market.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID")
    public ResponseEntity<?> findUserById(
            @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        UserResponse userResponse = userService.findUserById(id);
        String message = messageSource.getMessage("user.found", new Object[]{id}, locale);
        return ResponseEntity.ok()
                .body(Map.of("message", message, "data", userResponse));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID", description = "Update an existing user by their unique ID")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        String message = messageSource.getMessage("user.updated", new Object[]{id}, locale);
        return ResponseEntity.ok()
                .body(Map.of("message", message, "data", updatedUser));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<?> getAllUsers(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Page<UserResponse> usersPage = userService.findAll(pageable);
        String message = messageSource.getMessage("user.list.retrieved", null, locale);
        return ResponseEntity.ok()
                .body(Map.of("message", message, "data", usersPage));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Delete a user by their unique ID")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID of the user to be deleted") @PathVariable Long id,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        userService.delete(id);
        String message = messageSource.getMessage("user.deleted", new Object[]{id}, locale);
        return ResponseEntity.ok()
                .body(Map.of("message", message));
    }

}