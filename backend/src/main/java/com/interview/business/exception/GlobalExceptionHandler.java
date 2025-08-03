package com.interview.business.exception;

import com.interview.business.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TranslationService translationService;

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorRes = new ErrorResponse(List.of(translationService.translate(ex.getMessageKey(), ex.getMessageArgs())),
                request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRes);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ErrorResponse errorRes = new ErrorResponse(errors, request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
    }

    @ExceptionHandler(value = {InvalidParameterException.class})
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException ex, HttpServletRequest request)
    {
        ErrorResponse errorRes = new ErrorResponse(List.of(translationService.translate(ex.getMessageKey(),
                ex.getMessageArgs())), request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request)
    {
        ErrorResponse errorRes = new ErrorResponse(List.of(ex.getMessage()), request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
    }

    @ExceptionHandler(value = {java.security.InvalidParameterException.class})
    protected ResponseEntity<ErrorResponse> handleInvalidParameterException(java.security.InvalidParameterException ex, HttpServletRequest request)
    {
        ErrorResponse errorRes = new ErrorResponse(List.of(ex.getMessage()), request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request)
    {
        ErrorResponse errorRes = new ErrorResponse(List.of(ex.getMessage()), request.getRequestURI());
        logException(ex, errorRes);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }

    protected void logException(Exception e, ErrorResponse error) {
        log.error("handling exception, logid: '{}'", error.getLogId(), e);
    }
}
