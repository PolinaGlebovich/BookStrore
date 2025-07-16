package com.example.market.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

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

}
