package com.aiinterview.common;

public final class Constants {
    private Constants() {}
    public static final class Topics {
        public static final String INTERVIEW_SUBMITTED = "interview_submitted";
        public static final String INTERVIEW_SUBMITTED_DLT = "interview_submitted.DLT";
        public static final String PAYMENT_COMPLETED = "payment_completed";
        public static final String PAYMENT_COMPLETED_DLT = "payment_completed.DLT";
        private Topics() {}
    }
    public static final class Headers {
        public static final String IDEMPOTENCY_KEY = "Idempotency-Key";
        private Headers() {}
    }
}
