package com.aiinterview.common.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InterviewSubmittedEvent(UUID eventId, UUID interviewId, UUID candidateId, String role, List<SubmittedAnswer> answers, Instant submittedAt) {
    public record SubmittedAnswer(UUID questionId, String question, String answer) {}
}
