package com.abrxu.upflow.controller;

import com.abrxu.upflow.models.user.dtos.UserCreationDTO;
import com.abrxu.upflow.models.user.dtos.UserResponseDTO;
import com.abrxu.upflow.models.user.dtos.UserUpdateDTO;
import com.abrxu.upflow.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreationDTO dto) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        return new ResponseEntity<>(userService.updateUser(dto), HttpStatus.OK);
    }

}
