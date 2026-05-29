package com.aiinterview.interview.service;

import com.aiinterview.common.Constants;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import com.aiinterview.interview.dto.InterviewDtos.*;
import com.aiinterview.interview.model.InterviewSession;
import com.aiinterview.interview.repository.InterviewSessionRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {
    private final InterviewSessionRepository interviews;
    private final KafkaTemplate<String, InterviewSubmittedEvent> kafka;
    public InterviewService(InterviewSessionRepository interviews, KafkaTemplate<String, InterviewSubmittedEvent> kafka) { this.interviews = interviews; this.kafka = kafka; }
    @Transactional
    public InterviewResponse create(CreateInterviewRequest request) { return toResponse(interviews.save(new InterviewSession(request.candidateId(), request.role()))); }
    @Cacheable(value = "interviewSessions", key = "#id")
    @Transactional(readOnly = true)
    public InterviewResponse get(UUID id) { return toResponse(find(id)); }
    @Transactional
    public InterviewResponse submit(UUID id, SubmitInterviewRequest request) {
        InterviewSession session = find(id);
        if (session.getStatus() == InterviewSession.Status.SUBMITTED) throw new IllegalStateException("Interview already submitted");
        request.answers().forEach(a -> session.addAnswer(a.question(), a.answer()));
        session.submit();
        InterviewSession saved = interviews.save(session);
        var eventAnswers = saved.getAnswers().stream().map(a -> new InterviewSubmittedEvent.SubmittedAnswer(a.getId(), a.getQuestion(), a.getAnswer())).toList();
        var event = new InterviewSubmittedEvent(UUID.randomUUID(), saved.getId(), saved.getCandidateId(), saved.getRole(), eventAnswers, Instant.now());
        kafka.send(Constants.Topics.INTERVIEW_SUBMITTED, saved.getId().toString(), event);
        return toResponse(saved);
    }
    private InterviewSession find(UUID id) { return interviews.findById(id).orElseThrow(() -> new IllegalArgumentException("Interview not found")); }
    private static InterviewResponse toResponse(InterviewSession s) { return new InterviewResponse(s.getId(), s.getCandidateId(), s.getRole(), s.getStatus().name(), s.getCreatedAt(), s.getSubmittedAt()); }
}
