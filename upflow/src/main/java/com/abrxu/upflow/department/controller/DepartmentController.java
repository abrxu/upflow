package com.abrxu.upflow.department.controller;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.department.dtos.DepartmentCreationDTO;
import com.abrxu.upflow.department.services.DepartmentManagementService;
import com.abrxu.upflow.department.services.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentManagementService departmentManagementService;
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentManagementService departmentManagementService, DepartmentService departmentService) {
        this.departmentManagementService = departmentManagementService;
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody DepartmentCreationDTO dto) {
        return new ResponseEntity<>(departmentManagementService.createDepartment(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Department> getDepartmentById(@RequestParam Long id) {
        return new ResponseEntity<>(departmentService.findDepartmentById(id), HttpStatus.OK);
    }

}
