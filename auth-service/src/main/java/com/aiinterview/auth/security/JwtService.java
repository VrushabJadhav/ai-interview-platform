package com.aiinterview.auth.security;

import com.aiinterview.auth.model.UserAccount;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long ttlSeconds;
    public JwtService(@Value("${security.jwt.secret}") String secret, @Value("${security.jwt.ttl-seconds:86400}") long ttlSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
    }
    public String issue(UserAccount user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getId().toString()).claim("email", user.getEmail()).claim("role", user.getRole().name())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(ttlSeconds))).signWith(key).compact();
    }
}
