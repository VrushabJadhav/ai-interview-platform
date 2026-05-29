package com.aiinterview.notification.messaging;

import com.aiinterview.common.Constants;
import com.aiinterview.common.event.*;
import com.aiinterview.notification.email.EmailSender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final EmailSender emailSender;
    public NotificationListener(EmailSender emailSender) { this.emailSender = emailSender; }
    @KafkaListener(topics = Constants.Topics.INTERVIEW_SUBMITTED, groupId = "notification-service")
    public void onInterviewSubmitted(InterviewSubmittedEvent event) { emailSender.send("candidate-" + event.candidateId() + "@example.com", "Interview submitted", "Your interview is queued for AI evaluation."); }
    @KafkaListener(topics = Constants.Topics.PAYMENT_COMPLETED, groupId = "notification-service")
    public void onPaymentCompleted(PaymentCompletedEvent event) { emailSender.send("customer-" + event.customerId() + "@example.com", "Payment completed", "Payment " + event.paymentId() + " completed for " + event.planCode()); }
}
