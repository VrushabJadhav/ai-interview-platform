CREATE TABLE subscription_plans (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL
);
CREATE TABLE payments (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    plan_code VARCHAR(50) NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL,
    checkout_session_id VARCHAR(120) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ
);
CREATE TABLE processed_payment_callbacks (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(160) NOT NULL UNIQUE,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
INSERT INTO subscription_plans (id, code, name, price, currency) VALUES
('11111111-1111-1111-1111-111111111111', 'FREE', 'Free Candidate Trial', 0.00, 'USD'),
('22222222-2222-2222-2222-222222222222', 'PRO', 'Pro Interview Practice', 29.00, 'USD'),
('33333333-3333-3333-3333-333333333333', 'TEAM', 'Recruiter Team Plan', 199.00, 'USD');
