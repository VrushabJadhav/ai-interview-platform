package com.aiinterview.interview.dto;

import com.aiinterview.interview.dto.InterviewDtos.CreateInterviewRequest;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class InterviewDtosTest {
    @Test void createRequestCarriesCandidateAndRole() {
        UUID id = UUID.randomUUID();
        var request = new CreateInterviewRequest(id, "Java Backend Engineer");
        assertThat(request.candidateId()).isEqualTo(id);
        assertThat(request.role()).contains("Java");
    }
}
