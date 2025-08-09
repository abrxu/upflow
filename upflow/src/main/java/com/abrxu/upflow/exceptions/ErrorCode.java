package com.abrxu.upflow.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INCORRECT_CREDENTIALS("incorrect_credentials", HttpStatus.BAD_REQUEST, "Invalid user credentials."),

    DEPARTMENT_NOT_FOUND("department_not_found", HttpStatus.NOT_FOUND, "Department not found."),

    USER_NOT_FOUND("user_not_found", HttpStatus.NOT_FOUND, "User not found."),
    USER_MANAGER_CANT_BE_AN_EMPLOYEE("user_manager_cant_be_an_employee", HttpStatus.CONFLICT, "The manager can't be also an employee."),;

    final String code;
    final HttpStatus status;
    final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
