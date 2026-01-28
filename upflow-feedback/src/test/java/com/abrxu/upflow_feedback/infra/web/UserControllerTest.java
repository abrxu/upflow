package com.abrxu.upflow_feedback.infra.web;

import com.abrxu.upflow_feedback.application.dto.request.CreateUserRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateUserRequest;
import com.abrxu.upflow_feedback.application.dto.response.UserResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.GlobalExceptionHandler;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.port.in.UserService;
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
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of(deptId));
        UserResponse response = new UserResponse(userId, "John Doe", "john@example.com", roleId, true, Set.of(deptId));

        when(userService.create(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void create_shouldReturn400_whenEmailInvalid() throws Exception {
        UUID roleId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("John Doe", "invalid-email", roleId, Set.of());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn409_whenEmailDuplicate() throws Exception {
        UUID roleId = UUID.randomUUID();
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", roleId, Set.of());

        when(userService.create(any())).thenThrow(new DuplicateResourceException("Email exists"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UserResponse response = new UserResponse(userId, "John Doe", "john@example.com", roleId, true, Set.of());

        when(userService.getById(userId)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getById_shouldReturn404_whenNotExists() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.getById(userId)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UserResponse response = new UserResponse(userId, "John Doe", "john@example.com", roleId, true, Set.of());

        when(userService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("john@example.com"));
    }

    @Test
    void getByDepartment_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UUID deptId = UUID.randomUUID();
        UserResponse response = new UserResponse(userId, "John Doe", "john@example.com", roleId, true, Set.of(deptId));

        when(userService.getByDepartment(deptId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/users")
                        .param("departmentId", deptId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void update_shouldReturn200_whenValid() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest("Jane Doe", null, null, null);
        UserResponse response = new UserResponse(userId, "Jane Doe", "john@example.com", roleId, true, Set.of());

        when(userService.update(eq(userId), any(UpdateUserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void delete_shouldReturn204_whenExists() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenNotExists() throws Exception {
        UUID userId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("Not found")).when(userService).delete(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}