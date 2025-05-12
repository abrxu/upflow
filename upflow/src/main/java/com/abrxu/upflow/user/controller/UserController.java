package com.abrxu.upflow.user.controller;

import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import com.abrxu.upflow.user.dtos.UserUpdateDTO;
import com.abrxu.upflow.user.services.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreationDTO dto) {
        return new ResponseEntity<>(userManagementService.createUser(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        return new ResponseEntity<>(userManagementService.updateUser(dto), HttpStatus.OK);
    }

}
