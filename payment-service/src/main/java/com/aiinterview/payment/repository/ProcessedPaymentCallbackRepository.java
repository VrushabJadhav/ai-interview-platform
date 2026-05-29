package com.aiinterview.payment.repository;

import com.aiinterview.payment.model.ProcessedPaymentCallback;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedPaymentCallbackRepository extends JpaRepository<ProcessedPaymentCallback, UUID> {
    boolean existsByIdempotencyKey(String idempotencyKey);
}
