package com.abrxu.upflow_feedback.infra.web;

import com.abrxu.upflow_feedback.application.dto.request.CreateFeedbackRequest;
import com.abrxu.upflow_feedback.application.dto.response.FeedbackResponse;
import com.abrxu.upflow_feedback.application.exception.GlobalExceptionHandler;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.port.in.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(FeedbackController.class)
@Import(GlobalExceptionHandler.class)
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedbackService feedbackService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        UUID feedbackId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        CreateFeedbackRequest request = new CreateFeedbackRequest("Great place!", 5, deptId);
        FeedbackResponse response = new FeedbackResponse(feedbackId, "Great place!", 5, deptId, createdAt);

        when(feedbackService.create(any(CreateFeedbackRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(feedbackId.toString()))
                .andExpect(jsonPath("$.message").value("Great place!"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void create_shouldReturn400_whenMessageBlank() throws Exception {
        UUID deptId = UUID.randomUUID();
        CreateFeedbackRequest request = new CreateFeedbackRequest("", 5, deptId);

        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenRatingOutOfRange() throws Exception {
        UUID deptId = UUID.randomUUID();
        CreateFeedbackRequest request = new CreateFeedbackRequest("Great!", 6, deptId);

        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn404_whenDepartmentNotExists() throws Exception {
        UUID deptId = UUID.randomUUID();
        CreateFeedbackRequest request = new CreateFeedbackRequest("Great!", 5, deptId);

        when(feedbackService.create(any())).thenThrow(new ResourceNotFoundException("Department not found"));

        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception {
        UUID feedbackId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        FeedbackResponse response = new FeedbackResponse(feedbackId, "Great place!", 5, deptId, createdAt);

        when(feedbackService.getById(feedbackId)).thenReturn(response);

        mockMvc.perform(get("/api/feedback/{id}", feedbackId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Great place!"));
    }

    @Test
    void getById_shouldReturn404_whenNotExists() throws Exception {
        UUID feedbackId = UUID.randomUUID();

        when(feedbackService.getById(feedbackId)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/feedback/{id}", feedbackId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        UUID feedbackId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        FeedbackResponse response = new FeedbackResponse(feedbackId, "Great place!", 5, deptId, createdAt);

        when(feedbackService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].message").value("Great place!"));
    }

    @Test
    void getByDepartment_shouldReturn200() throws Exception {
        UUID feedbackId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        Instant createdAt = Instant.now();
        FeedbackResponse response = new FeedbackResponse(feedbackId, "Great place!", 5, deptId, createdAt);

        when(feedbackService.getByDepartment(eq(deptId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/feedback")
                        .param("departmentId", deptId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].message").value("Great place!"));
    }

    @Test
    void delete_shouldReturn204_whenExists() throws Exception {
        UUID feedbackId = UUID.randomUUID();

        doNothing().when(feedbackService).delete(feedbackId);

        mockMvc.perform(delete("/api/feedback/{id}", feedbackId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenNotExists() throws Exception {
        UUID feedbackId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("Not found")).when(feedbackService).delete(feedbackId);

        mockMvc.perform(delete("/api/feedback/{id}", feedbackId))
                .andExpect(status().isNotFound());
    }
}