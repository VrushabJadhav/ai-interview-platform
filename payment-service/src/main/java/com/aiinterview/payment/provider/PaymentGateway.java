package com.aiinterview.payment.provider;

import java.util.UUID;

public interface PaymentGateway { CheckoutSession createCheckoutSession(UUID customerId, String planCode); record CheckoutSession(String id, String url) {} }
