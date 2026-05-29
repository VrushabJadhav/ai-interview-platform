package com.aiinterview.ai.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evaluation_results")
public class EvaluationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @Column(nullable = false, unique = true) private UUID interviewId;
    @Column(nullable = false) private UUID candidateId;
    private int correctness; private int clarity; private int depth; private int communication;
    @Column(nullable = false, columnDefinition = "TEXT") private String summary;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected EvaluationResult() {}
    public EvaluationResult(UUID interviewId, UUID candidateId, int correctness, int clarity, int depth, int communication, String summary) {
        this.interviewId = interviewId; this.candidateId = candidateId; this.correctness = correctness; this.clarity = clarity; this.depth = depth; this.communication = communication; this.summary = summary;
    }
    public UUID getId() { return id; } public UUID getInterviewId() { return interviewId; } public UUID getCandidateId() { return candidateId; }
    public int getCorrectness() { return correctness; } public int getClarity() { return clarity; } public int getDepth() { return depth; } public int getCommunication() { return communication; }
    public String getSummary() { return summary; } public Instant getCreatedAt() { return createdAt; }
}
