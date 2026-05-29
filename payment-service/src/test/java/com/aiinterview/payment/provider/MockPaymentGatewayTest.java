package com.aiinterview.payment.provider;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class MockPaymentGatewayTest {
    @Test void createsMockCheckoutUrl() {
        var session = new MockPaymentGateway().createCheckoutSession(UUID.randomUUID(), "PRO");
        assertThat(session.id()).startsWith("mock_cs_");
        assertThat(session.url()).contains("PRO");
    }
}
