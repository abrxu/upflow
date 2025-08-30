package com.abrxu.upflow.exceptions;

import lombok.Getter;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class ErrorCodeException extends HttpStatusCodeException {

    protected final ErrorCode errorCode;
    protected final String message;

    public ErrorCodeException(ErrorCode errorCode, String... args) {
        super(errorCode.getStatus(), errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = formatMessage(errorCode.getMessage(), args);
    }

    private String formatMessage(String message, String... args) {
        if (args == null || args.length == 0) {
            return message;
        }
        for (int i = 0; i < args.length; i++) {
            message = message.replace(args[i], args[i + 1]);
        }
        return message;
    }

}
