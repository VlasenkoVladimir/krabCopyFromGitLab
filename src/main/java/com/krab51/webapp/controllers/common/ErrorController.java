package com.krab51.webapp.controllers.common;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    protected final Logger logger = getLogger(ErrorController.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                createProblemDetail(ex, INTERNAL_SERVER_ERROR, ex.getMessage(), null, null, request),
                new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }
}