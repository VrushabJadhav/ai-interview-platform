package com.aiinterview.payment.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false, unique = true) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private BigDecimal price;
    @Column(nullable = false) private String currency;
    protected SubscriptionPlan() {}
    public UUID getId(){return id;} public String getCode(){return code;} public String getName(){return name;} public BigDecimal getPrice(){return price;} public String getCurrency(){return currency;}
}
