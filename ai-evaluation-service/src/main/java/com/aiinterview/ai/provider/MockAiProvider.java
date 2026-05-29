package com.aiinterview.ai.provider;

import com.aiinterview.common.dto.EvaluationScoreDto;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "mock", matchIfMissing = true)
public class MockAiProvider implements AiProvider {
    @Override public EvaluationScoreDto evaluate(InterviewSubmittedEvent event) {
        int answerCount = event.answers() == null ? 0 : event.answers().size();
        int base = Math.min(90, 60 + answerCount * 8);
        return new EvaluationScoreDto(base, base - 3, base - 5, base - 2, "Mock evaluation completed for " + event.role() + ". Replace with OpenAiProvider in production.");
    }
}
