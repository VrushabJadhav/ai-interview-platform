package com.aiinterview.interview.controller;

import com.aiinterview.interview.dto.InterviewDtos.*;
import com.aiinterview.interview.service.InterviewService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService service;
    public InterviewController(InterviewService service) { this.service = service; }
    @PostMapping InterviewResponse create(@Valid @RequestBody CreateInterviewRequest request) { return service.create(request); }
    @GetMapping("/{id}") InterviewResponse get(@PathVariable UUID id) { return service.get(id); }
    @PostMapping("/{id}/submit") InterviewResponse submit(@PathVariable UUID id, @Valid @RequestBody SubmitInterviewRequest request) { return service.submit(id, request); }
}
