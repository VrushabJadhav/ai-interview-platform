package com.aiinterview.ai.provider;

import com.aiinterview.common.dto.EvaluationScoreDto;
import com.aiinterview.common.event.InterviewSubmittedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
public class OpenAiProvider implements AiProvider {
    private final String apiKey;
    public OpenAiProvider(@Value("${OPENAI_API_KEY:}") String apiKey) { this.apiKey = apiKey; }
    @Override public EvaluationScoreDto evaluate(InterviewSubmittedEvent event) {
        if (apiKey == null || apiKey.isBlank()) throw new IllegalStateException("OPENAI_API_KEY is required when ai.provider=openai");
        return new EvaluationScoreDto(80, 80, 80, 80, "OpenAI provider placeholder: wire the official SDK/client here.");
    }
}
