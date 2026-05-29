package com.aiinterview.payment.service;

import com.aiinterview.common.Constants;
import com.aiinterview.common.event.PaymentCompletedEvent;
import com.aiinterview.payment.dto.PaymentDtos.*;
import com.aiinterview.payment.model.*;
import com.aiinterview.payment.provider.PaymentGateway;
import com.aiinterview.payment.repository.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentRepository payments; private final SubscriptionPlanRepository plans; private final ProcessedPaymentCallbackRepository callbacks; private final PaymentGateway gateway; private final KafkaTemplate<String, PaymentCompletedEvent> kafka;
    public PaymentService(PaymentRepository payments, SubscriptionPlanRepository plans, ProcessedPaymentCallbackRepository callbacks, PaymentGateway gateway, KafkaTemplate<String, PaymentCompletedEvent> kafka) { this.payments=payments; this.plans=plans; this.callbacks=callbacks; this.gateway=gateway; this.kafka=kafka; }
    @Transactional
    public CheckoutResponse createCheckout(CheckoutRequest request) {
        SubscriptionPlan plan = plans.findByCode(request.planCode()).orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        var session = gateway.createCheckoutSession(request.customerId(), plan.getCode());
        Payment payment = payments.save(new Payment(request.customerId(), plan.getCode(), plan.getPrice(), plan.getCurrency(), session.id()));
        return new CheckoutResponse(payment.getId(), session.id(), session.url());
    }
    @Transactional
    public PaymentResponse complete(String idempotencyKey, PaymentCallbackRequest request) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) throw new IllegalArgumentException("Idempotency-Key header is required");
        Payment payment = payments.findByCheckoutSessionId(request.checkoutSessionId()).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (callbacks.existsByIdempotencyKey(idempotencyKey)) return toResponse(payment);
        callbacks.save(new ProcessedPaymentCallback(idempotencyKey));
        if (payment.getStatus() != Payment.Status.COMPLETED) {
            payment.complete();
            kafka.send(Constants.Topics.PAYMENT_COMPLETED, payment.getId().toString(), new PaymentCompletedEvent(UUID.randomUUID(), payment.getId(), payment.getCustomerId(), payment.getPlanCode(), payment.getAmount(), payment.getCurrency(), Instant.now()));
        }
        return toResponse(payment);
    }
    private static PaymentResponse toResponse(Payment p) { return new PaymentResponse(p.getId(), p.getStatus().name(), p.getAmount(), p.getCurrency()); }
}
