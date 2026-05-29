package com.aiinterview.ai.service;

import com.aiinterview.ai.model.EvaluationResult;
import com.aiinterview.ai.provider.AiProvider;
import com.aiinterview.ai.repository.EvaluationResultRepository;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EvaluationService {
    private final EvaluationResultRepository repository;
    private final AiProvider provider;
    public EvaluationService(EvaluationResultRepository repository, AiProvider provider) { this.repository = repository; this.provider = provider; }
    @Transactional
    public EvaluationResult evaluate(InterviewSubmittedEvent event) {
        return repository.findByInterviewId(event.interviewId()).orElseGet(() -> {
            var score = provider.evaluate(event);
            return repository.save(new EvaluationResult(event.interviewId(), event.candidateId(), score.correctness(), score.clarity(), score.depth(), score.communication(), score.summary()));
        });
    }
    @Cacheable(value = "evaluationResults", key = "#interviewId")
    @Transactional(readOnly = true)
    public EvaluationResult getByInterviewId(UUID interviewId) { return repository.findByInterviewId(interviewId).orElseThrow(() -> new IllegalArgumentException("Evaluation not found")); }
}
