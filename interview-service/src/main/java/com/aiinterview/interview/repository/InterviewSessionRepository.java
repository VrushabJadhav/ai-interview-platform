package com.aiinterview.interview.repository;

import com.aiinterview.interview.model.InterviewSession;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, UUID> {}
