package com.aiinterview.auth.security;

import com.aiinterview.auth.model.UserAccount;
import com.aiinterview.common.Role;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    @Test void issuesSignedToken() {
        JwtService service = new JwtService("change-this-dev-secret-change-this-dev-secret", 3600);
        var user = new UserAccount("candidate@example.com", "hash", Role.CANDIDATE);
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        String token = service.issue(user);
        assertThat(token).contains(".");
    }
}
