package com.aiinterview.auth.dto;

import com.aiinterview.common.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@Email @NotBlank String email, @Size(min = 8) String password, @NotNull Role role) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String token, UUID userId, String email, Role role) {}
}
