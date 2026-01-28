package com.abrxu.upflow_feedback.infra.web;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import com.abrxu.upflow_feedback.application.port.in.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> create(@Valid @RequestBody CreateFeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(feedbackService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<FeedbackResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getAll(pageable));
    }

    @GetMapping(params = "departmentId")
    public ResponseEntity<Page<FeedbackResponse>> getByDepartment(@RequestParam UUID departmentId, Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getByDepartment(departmentId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        feedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }

}