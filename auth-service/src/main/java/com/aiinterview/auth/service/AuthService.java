package com.aiinterview.auth.service;

import com.aiinterview.auth.dto.AuthDtos.*;
import com.aiinterview.auth.model.UserAccount;
import com.aiinterview.auth.repository.UserRepository;
import com.aiinterview.auth.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.encoder = encoder; this.jwt = jwt; }
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) throw new IllegalArgumentException("Email already registered");
        UserAccount user = users.save(new UserAccount(request.email().toLowerCase(), encoder.encode(request.password()), request.role()));
        return new AuthResponse(jwt.issue(user), user.getId(), user.getEmail(), user.getRole());
    }
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserAccount user = users.findByEmail(request.email().toLowerCase()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!encoder.matches(request.password(), user.getPasswordHash())) throw new BadCredentialsException("Invalid credentials");
        return new AuthResponse(jwt.issue(user), user.getId(), user.getEmail(), user.getRole());
    }
}
