package com.aiinterview.ai.controller;

import com.aiinterview.ai.model.EvaluationResult;
import com.aiinterview.ai.service.EvaluationService;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService service;
    public EvaluationController(EvaluationService service) { this.service = service; }
    @GetMapping("/interviews/{interviewId}") EvaluationResult get(@PathVariable UUID interviewId) { return service.getByInterviewId(interviewId); }
}
