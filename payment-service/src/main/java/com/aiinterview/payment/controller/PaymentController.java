package com.aiinterview.payment.controller;

import com.aiinterview.common.Constants;
import com.aiinterview.payment.dto.PaymentDtos.*;
import com.aiinterview.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service) { this.service = service; }
    @PostMapping("/checkout") CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) { return service.createCheckout(request); }
    @PostMapping("/callbacks/mock") PaymentResponse callback(@RequestHeader(Constants.Headers.IDEMPOTENCY_KEY) String idempotencyKey, @Valid @RequestBody PaymentCallbackRequest request) { return service.complete(idempotencyKey, request); }
}
