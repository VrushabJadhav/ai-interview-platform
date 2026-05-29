package com.aiinterview.ai.provider;

import com.aiinterview.common.dto.EvaluationScoreDto;
import com.aiinterview.common.event.InterviewSubmittedEvent;

public interface AiProvider { EvaluationScoreDto evaluate(InterviewSubmittedEvent event); }
