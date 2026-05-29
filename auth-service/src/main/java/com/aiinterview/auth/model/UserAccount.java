package com.aiinterview.auth.model;

import com.aiinterview.common.Role;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();
    protected UserAccount() {}
    public UserAccount(String email, String passwordHash, Role role) { this.email = email; this.passwordHash = passwordHash; this.role = role; }
    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
}
