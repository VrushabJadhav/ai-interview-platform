package com.aiinterview.interview.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "interview_sessions")
public class InterviewSession {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false) private UUID candidateId;
    @Column(nullable = false) private String role;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.CREATED;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    private Instant submittedAt;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateAnswer> answers = new ArrayList<>();
    public enum Status { CREATED, SUBMITTED }
    protected InterviewSession() {}
    public InterviewSession(UUID candidateId, String role) { this.candidateId = candidateId; this.role = role; }
    public void addAnswer(String question, String answer) { answers.add(new CandidateAnswer(this, question, answer)); }
    public void submit() { this.status = Status.SUBMITTED; this.submittedAt = Instant.now(); }
    public UUID getId() { return id; }
    public UUID getCandidateId() { return candidateId; }
    public String getRole() { return role; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getSubmittedAt() { return submittedAt; }
    public List<CandidateAnswer> getAnswers() { return answers; }
}
