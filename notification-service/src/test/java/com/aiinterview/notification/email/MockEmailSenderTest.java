package com.aiinterview.notification.email;

import org.junit.jupiter.api.Test;

class MockEmailSenderTest {
    @Test void sendsWithoutExternalProvider() { new MockEmailSender().send("test@example.com", "Subject", "Body"); }
}
