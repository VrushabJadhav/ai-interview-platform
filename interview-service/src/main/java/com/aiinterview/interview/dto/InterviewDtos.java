package com.aiinterview.interview.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class InterviewDtos {
    private InterviewDtos() {}
    public record CreateInterviewRequest(@NotNull UUID candidateId, @NotBlank String role) {}
    public record AnswerRequest(@NotBlank String question, @NotBlank String answer) {}
    public record SubmitInterviewRequest(@NotEmpty List<@Valid AnswerRequest> answers) {}
    public record InterviewResponse(UUID id, UUID candidateId, String role, String status, Instant createdAt, Instant submittedAt) implements Serializable {}
}
