package com.aiinterview.payment.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_payment_callbacks")
public class ProcessedPaymentCallback {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false, unique = true) private String idempotencyKey;
    @Column(nullable = false) private Instant processedAt = Instant.now();
    protected ProcessedPaymentCallback() {}
    public ProcessedPaymentCallback(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}
