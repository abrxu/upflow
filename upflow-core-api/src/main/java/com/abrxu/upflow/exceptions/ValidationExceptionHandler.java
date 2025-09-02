package com.abrxu.upflow.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .toList();

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<Map<String, Object>> handleErrorCodeException(ErrorCodeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", ex.getErrorCode().code);
        errorResponse.put("status", ex.getErrorCode().getStatus().value());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> httpMessageNotReadableException(HttpMessageNotReadableException ex) {

        String specificMessage = ErrorCode.INVALID_REQUEST_BODY.getMessage();

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                String fieldName = ifx.getPath()
                        .stream()
                        .map(ref -> ref.getFieldName())
                        .findFirst().orElse("Unknown field");

                String acceptdValues = Arrays.toString(ifx.getTargetType().getEnumConstants());

                specificMessage = String.format(
                        "Invalid value '%s' for field '%s'. Accepted values are: %s",
                        ifx.getValue(),
                        fieldName,
                        acceptdValues
                );
            }
        }

        Map<String, Object> errorResponse = new HashMap<>();
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
        errorResponse.put("code", errorCode.code);
        errorResponse.put("status", errorCode.getStatus().value());
        errorResponse.put("message", specificMessage);

        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
