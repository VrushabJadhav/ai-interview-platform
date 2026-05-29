package com.aiinterview.interview.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "candidate_answers")
public class CandidateAnswer {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "interview_id")
    private InterviewSession session;
    @Column(nullable = false, columnDefinition = "TEXT") private String question;
    @Column(nullable = false, columnDefinition = "TEXT") private String answer;
    protected CandidateAnswer() {}
    CandidateAnswer(InterviewSession session, String question, String answer) { this.session = session; this.question = question; this.answer = answer; }
    public UUID getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
}
