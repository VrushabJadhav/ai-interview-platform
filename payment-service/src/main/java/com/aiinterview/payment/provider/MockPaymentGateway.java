package com.aiinterview.payment.provider;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MockPaymentGateway implements PaymentGateway {
    @Override public CheckoutSession createCheckoutSession(UUID customerId, String planCode) {
        String id = "mock_cs_" + UUID.randomUUID();
        return new CheckoutSession(id, "https://payments.local/checkout/" + id + "?plan=" + planCode);
    }
}
