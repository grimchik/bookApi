package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
