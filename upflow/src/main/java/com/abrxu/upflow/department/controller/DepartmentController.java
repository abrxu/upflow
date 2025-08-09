package com.abrxu.upflow.department.controller;

import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.dtos.DepartmentEditDTO;
import com.abrxu.upflow.department.dtos.DepartmentResponseDTO;
import com.abrxu.upflow.department.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentCreationDTO dto) {
        return new ResponseEntity<>(departmentService.createDepartment(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDTO> getDepartment(
            @PathVariable("departmentId") Long departmentId,
            @Valid @RequestBody DepartmentEditDTO dto
            ) {
        return new ResponseEntity<>(departmentService.editDepartment(dto, departmentId), HttpStatus.OK);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentResponseDTO> getDepartment(@PathVariable("departmentId") Long departmentId) {
        return new ResponseEntity<>(departmentService.getDepartment(departmentId), HttpStatus.OK);
    }

}
