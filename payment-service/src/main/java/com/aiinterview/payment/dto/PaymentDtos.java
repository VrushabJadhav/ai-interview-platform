package com.aiinterview.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public final class PaymentDtos {
    private PaymentDtos() {}
    public record CheckoutRequest(@NotNull UUID customerId, @NotBlank String planCode) {}
    public record CheckoutResponse(UUID paymentId, String checkoutSessionId, String checkoutUrl) {}
    public record PaymentCallbackRequest(@NotBlank String checkoutSessionId) {}
    public record PaymentResponse(UUID paymentId, String status, BigDecimal amount, String currency) {}
}
