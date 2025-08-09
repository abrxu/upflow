package com.abrxu.upflow.user.controller;

import com.abrxu.upflow.user.dtos.UserCreationDTO;
import com.abrxu.upflow.user.dtos.UserEditDTO;
import com.abrxu.upflow.user.dtos.UserResponseDTO;
import com.abrxu.upflow.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getPaginatedUsers(
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        return new ResponseEntity<>(userService.getPaginatedUsers(search, pageable), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> editUser(
            @Valid @RequestBody UserEditDTO dto,
            @PathVariable("userId") Long userId
    ) {
        return new ResponseEntity<>(userService.editUser(dto, userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(
            @PathVariable("userId") Long userId
    ) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(
            @PathVariable("userId") Long userId
    ) {
        userService.deleteUser(userId);
    }

}
