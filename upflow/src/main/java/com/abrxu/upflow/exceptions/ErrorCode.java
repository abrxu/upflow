package com.abrxu.upflow.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INCORRECT_CREDENTIALS("incorrect_credentials", HttpStatus.BAD_REQUEST, "Invalid user credentials."),
    USER_NOT_FOUND("user_not_found", HttpStatus.NOT_FOUND, "User not found."),

    MANAGER_CANT_BE_AN_EMPLOYEE("manager_cant_be_an_employee", HttpStatus.CONFLICT, "The manager can't be also an employee."),;

    final String code;
    final HttpStatus status;
    final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
