package com.abrxu.upflow_feedback.infra.web;

import com.abrxu.upflow_feedback.application.dto.request.CreateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateDepartmentRequest;
import com.abrxu.upflow_feedback.application.dto.response.DepartmentResponse;
import com.abrxu.upflow_feedback.application.exception.GlobalExceptionHandler;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.port.in.DepartmentService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DepartmentController.class)
@Import(GlobalExceptionHandler.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        UUID deptId = UUID.randomUUID();
        CreateDepartmentRequest request = new CreateDepartmentRequest("Engineering", "Tech dept");
        DepartmentResponse response = new DepartmentResponse(deptId, "Engineering", "Tech dept");

        when(departmentService.create(any(CreateDepartmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(deptId.toString()))
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void create_shouldReturn400_whenNameBlank() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest("", "Tech dept");

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception {
        UUID deptId = UUID.randomUUID();
        DepartmentResponse response = new DepartmentResponse(deptId, "Engineering", "Tech dept");

        when(departmentService.getById(deptId)).thenReturn(response);

        mockMvc.perform(get("/api/departments/{id}", deptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void getById_shouldReturn404_whenNotExists() throws Exception {
        UUID deptId = UUID.randomUUID();

        when(departmentService.getById(deptId)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/departments/{id}", deptId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        UUID deptId = UUID.randomUUID();
        DepartmentResponse response = new DepartmentResponse(deptId, "Engineering", "Tech dept");

        when(departmentService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Engineering"));
    }

    @Test
    void update_shouldReturn200_whenValid() throws Exception {
        UUID deptId = UUID.randomUUID();
        UpdateDepartmentRequest request = new UpdateDepartmentRequest("HR", "Human Resources");
        DepartmentResponse response = new DepartmentResponse(deptId, "HR", "Human Resources");

        when(departmentService.update(eq(deptId), any(UpdateDepartmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/departments/{id}", deptId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void delete_shouldReturn204_whenExists() throws Exception {
        UUID deptId = UUID.randomUUID();

        doNothing().when(departmentService).delete(deptId);

        mockMvc.perform(delete("/api/departments/{id}", deptId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn400_whenConstraintViolation() throws Exception {
        UUID deptId = UUID.randomUUID();

        doThrow(new IllegalArgumentException("Cannot delete")).when(departmentService).delete(deptId);

        mockMvc.perform(delete("/api/departments/{id}", deptId))
                .andExpect(status().isBadRequest());
    }
}