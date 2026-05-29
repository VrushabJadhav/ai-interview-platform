package com.aiinterview.ai.messaging;

import com.aiinterview.ai.service.EvaluationService;
import com.aiinterview.common.Constants;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InterviewSubmittedListener {
    private final EvaluationService service;
    public InterviewSubmittedListener(EvaluationService service) { this.service = service; }
    @KafkaListener(topics = Constants.Topics.INTERVIEW_SUBMITTED, groupId = "ai-evaluation-service")
    public void onInterviewSubmitted(InterviewSubmittedEvent event) { service.evaluate(event); }
}
