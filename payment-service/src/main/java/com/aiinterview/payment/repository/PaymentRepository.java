package com.aiinterview.payment.repository;

import com.aiinterview.payment.model.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByCheckoutSessionId(String checkoutSessionId);
}
