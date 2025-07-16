package com.example.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Schema(description = "User's name", example = "Alexander")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s]{2,50}$", message = "Name must contain only letters and spaces")
    @NotBlank(message = "Username cannot be empty")
    private String name;

    @Schema(description = "User's nickname", example = "alex.smith01")
    @Size(min = 5, max = 50, message = "Nickname must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._]{3,15}$", message = "Nickname must contain only letters, numbers, dots, and underscores")
    @NotBlank(message = "Nickname cannot be empty")
    private String username;

    @Schema(description = "User's password", example = "Strong1Pass!")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must meet the following requirements: at least 6 characters, at least one lowercase letter, " +
                    "at least one uppercase letter, at least one digit, and at least one special character")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Size(min = 1, max = 2, message = "Roles must contain between 1 and 2 items")
    private Set<String> roles;
}