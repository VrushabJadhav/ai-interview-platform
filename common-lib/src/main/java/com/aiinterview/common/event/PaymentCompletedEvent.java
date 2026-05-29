package com.aiinterview.common.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(UUID eventId, UUID paymentId, UUID customerId, String planCode, BigDecimal amount, String currency, Instant completedAt) {}
