package com.abrxu.upflow.controller;

import com.abrxu.upflow.models.department.Department;
import com.abrxu.upflow.models.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.services.DepartmentService;
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
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody DepartmentCreationDTO dto) {
        return new ResponseEntity<>(departmentService.createDepartment(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Department> getDepartmentById(@RequestParam Long id) {
        return new ResponseEntity<>(departmentService.findDepartmentById(id), HttpStatus.OK);
    }

}
