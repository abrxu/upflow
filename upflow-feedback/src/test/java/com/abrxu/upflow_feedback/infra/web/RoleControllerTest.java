package com.abrxu.upflow_feedback.infra.web;

import com.abrxu.upflow_feedback.application.dto.request.CreateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.request.UpdateRoleRequest;
import com.abrxu.upflow_feedback.application.dto.response.RoleResponse;
import com.abrxu.upflow_feedback.application.exception.DuplicateResourceException;
import com.abrxu.upflow_feedback.application.exception.GlobalExceptionHandler;
import com.abrxu.upflow_feedback.application.exception.ResourceNotFoundException;
import com.abrxu.upflow_feedback.application.port.in.RoleService;
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
@WebMvcTest(RoleController.class)
@Import(GlobalExceptionHandler.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void create_shouldReturn201_whenValid() throws Exception {
        UUID roleId = UUID.randomUUID();
        CreateRoleRequest request = new CreateRoleRequest("ADMIN");
        RoleResponse response = new RoleResponse(roleId, "ADMIN");

        when(roleService.create(any(CreateRoleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void create_shouldReturn400_whenNameBlank() throws Exception {
        CreateRoleRequest request = new CreateRoleRequest("");

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn409_whenDuplicate() throws Exception {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN");

        when(roleService.create(any())).thenThrow(new DuplicateResourceException("Role exists"));

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getById_shouldReturn200_whenExists() throws Exception {
        UUID roleId = UUID.randomUUID();
        RoleResponse response = new RoleResponse(roleId, "ADMIN");

        when(roleService.getById(roleId)).thenReturn(response);

        mockMvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void getById_shouldReturn404_whenNotExists() throws Exception {
        UUID roleId = UUID.randomUUID();

        when(roleService.getById(roleId)).thenThrow(new ResourceNotFoundException("Role not found"));

        mockMvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_shouldReturn200() throws Exception {
        UUID roleId = UUID.randomUUID();
        RoleResponse response = new RoleResponse(roleId, "ADMIN");

        when(roleService.getAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("ADMIN"));
    }

    @Test
    void update_shouldReturn200_whenValid() throws Exception {
        UUID roleId = UUID.randomUUID();
        UpdateRoleRequest request = new UpdateRoleRequest("MANAGER");
        RoleResponse response = new RoleResponse(roleId, "MANAGER");

        when(roleService.update(eq(roleId), any(UpdateRoleRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MANAGER"));
    }

    @Test
    void delete_shouldReturn204_whenExists() throws Exception {
        UUID roleId = UUID.randomUUID();

        doNothing().when(roleService).delete(roleId);

        mockMvc.perform(delete("/api/roles/{id}", roleId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenNotExists() throws Exception {
        UUID roleId = UUID.randomUUID();

        doThrow(new ResourceNotFoundException("Role not found")).when(roleService).delete(roleId);

        mockMvc.perform(delete("/api/roles/{id}", roleId))
                .andExpect(status().isNotFound());
    }
}