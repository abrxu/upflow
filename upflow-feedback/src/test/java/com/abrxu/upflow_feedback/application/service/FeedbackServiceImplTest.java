package com.abrxu.upflow_feedback.application.service;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.mapper.FeedbackMapper;
import com.abrxu.upflow_feedback.application.port.out.DepartmentRepository;
import com.abrxu.upflow_feedback.application.port.out.FeedbackRepository;
import com.abrxu.upflow_feedback.domain.Feedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private UUID feedbackId;
    private UUID departmentId;
    private Feedback feedback;
    private FeedbackResponse feedbackResponse;
    private Instant createdAt;

    @BeforeEach
    void setUp() {
        feedbackId = UUID.randomUUID();
        departmentId = UUID.randomUUID();
        createdAt = Instant.now();
        feedback = new Feedback(feedbackId, "Great workplace!", 5, departmentId, createdAt);
        feedbackResponse = new FeedbackResponse(feedbackId, "Great workplace!", 5, departmentId, createdAt);
    }

    @Test
    void create_shouldCreateFeedback_whenDepartmentExists() {
        CreateFeedbackRequest request = new CreateFeedbackRequest("Great workplace!", 5, departmentId);
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(feedbackMapper.toDomain(request)).thenReturn(feedback);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);
        when(feedbackMapper.toResponse(feedback)).thenReturn(feedbackResponse);

        FeedbackResponse result = feedbackService.create(request);

        assertNotNull(result);
        assertEquals("Great workplace!", result.message());
        assertEquals(5, result.rating());
        verify(feedbackRepository).save(feedback);
    }

    @Test
    void create_shouldThrowResourceNotFoundException_whenDepartmentNotExists() {
        CreateFeedbackRequest request = new CreateFeedbackRequest("Great workplace!", 5, departmentId);
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> feedbackService.create(request));
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnFeedback_whenExists() {
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));
        when(feedbackMapper.toResponse(feedback)).thenReturn(feedbackResponse);

        FeedbackResponse result = feedbackService.getById(feedbackId);

        assertNotNull(result);
        assertEquals(feedbackId, result.id());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenNotExists() {
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> feedbackService.getById(feedbackId));
    }

    @Test
    void getAll_shouldReturnPagedFeedback() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> page = new PageImpl<>(List.of(feedback));
        when(feedbackRepository.findAll(pageable)).thenReturn(page);
        when(feedbackMapper.toResponse(feedback)).thenReturn(feedbackResponse);

        Page<FeedbackResponse> result = feedbackService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByDepartment_shouldReturnPagedFeedback_whenDepartmentExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> page = new PageImpl<>(List.of(feedback));
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(feedbackRepository.findByDepartmentId(departmentId, pageable)).thenReturn(page);
        when(feedbackMapper.toResponse(feedback)).thenReturn(feedbackResponse);

        Page<FeedbackResponse> result = feedbackService.getByDepartment(departmentId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByDepartment_shouldThrowResourceNotFoundException_whenDepartmentNotExists() {
        Pageable pageable = PageRequest.of(0, 10);
        when(departmentRepository.existsById(departmentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> feedbackService.getByDepartment(departmentId, pageable));
    }

    @Test
    void delete_shouldDeleteFeedback_whenExists() {
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));

        feedbackService.delete(feedbackId);

        verify(feedbackRepository).deleteById(feedbackId);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenNotExists() {
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> feedbackService.delete(feedbackId));
        verify(feedbackRepository, never()).deleteById(any());
    }
    
}