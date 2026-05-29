package com.aiinterview.payment.repository;

import com.aiinterview.payment.model.SubscriptionPlan;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
    Optional<SubscriptionPlan> findByCode(String code);
}
