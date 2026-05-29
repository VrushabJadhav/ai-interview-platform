package com.aiinterview.ai.provider;

import com.aiinterview.common.event.InterviewSubmittedEvent;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class MockAiProviderTest {
    @Test void evaluatesWithoutExternalKey() {
        var provider = new MockAiProvider();
        var event = new InterviewSubmittedEvent(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Java", List.of(), Instant.now());
        assertThat(provider.evaluate(event).summary()).contains("Mock evaluation");
    }
}
