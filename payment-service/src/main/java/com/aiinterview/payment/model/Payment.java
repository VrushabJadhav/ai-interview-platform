package com.aiinterview.payment.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false) private UUID customerId;
    @Column(nullable = false) private String planCode;
    @Column(nullable = false) private BigDecimal amount;
    @Column(nullable = false) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status;
    @Column(nullable = false, unique = true) private String checkoutSessionId;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    private Instant completedAt;
    public enum Status { PENDING, COMPLETED }
    protected Payment() {}
    public Payment(UUID customerId, String planCode, BigDecimal amount, String currency, String checkoutSessionId) { this.customerId=customerId; this.planCode=planCode; this.amount=amount; this.currency=currency; this.checkoutSessionId=checkoutSessionId; this.status=Status.PENDING; }
    public void complete(){this.status=Status.COMPLETED; this.completedAt=Instant.now();}
    public UUID getId(){return id;} public UUID getCustomerId(){return customerId;} public String getPlanCode(){return planCode;} public BigDecimal getAmount(){return amount;} public String getCurrency(){return currency;} public Status getStatus(){return status;} public String getCheckoutSessionId(){return checkoutSessionId;} public Instant getCompletedAt(){return completedAt;}
}
