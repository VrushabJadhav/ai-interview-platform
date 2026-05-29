package com.aiinterview.common;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConstantsTest {
    @Test void topicsAreStableContracts() {
        assertThat(Constants.Topics.INTERVIEW_SUBMITTED).isEqualTo("interview_submitted");
        assertThat(Constants.Topics.PAYMENT_COMPLETED).isEqualTo("payment_completed");
    }
}
