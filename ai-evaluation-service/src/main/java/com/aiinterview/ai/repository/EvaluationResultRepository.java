package com.aiinterview.ai.repository;

import com.aiinterview.ai.model.EvaluationResult;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationResultRepository extends JpaRepository<EvaluationResult, UUID> {
    Optional<EvaluationResult> findByInterviewId(UUID interviewId);
    boolean existsByInterviewId(UUID interviewId);
}
