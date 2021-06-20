package com.mastersessay.blockchain.accounting.configuration;

import com.mastersessay.blockchain.accounting.dto.response.error.ErrorResponse;
import com.mastersessay.blockchain.accounting.dto.response.error.ValidationErrorResponse;
import com.mastersessay.blockchain.accounting.dto.response.error.Violation;
import com.mastersessay.blockchain.accounting.dto.response.error.ViolationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.UNAUTHORIZED_ERROR_MESSAGE;

@RestControllerAdvice
@SuppressWarnings("Duplicates")
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler({
            IllegalArgumentException.class,
            PropertyReferenceException.class
    })
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        log.error("Exception occurred: {}", ex);
        log.info("Response code = {}", HttpStatus.BAD_REQUEST);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("Exception occurred: {}", ex);
        log.info("Response code = {}", HttpStatus.BAD_REQUEST);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<Violation> errorMessages = new ArrayList<>();

        ex
                .getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorMessages.add(Violation
                        .builder()
                        .fieldName(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()));

        log.error("Validation errors: {}", errorMessages.toString());
        log.info("Response code = {}", HttpStatus.BAD_REQUEST);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ValidationErrorResponse
                        .builder()
                        .violations(errorMessages)
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }

    @ExceptionHandler({
            ConstraintViolationException.class
    })
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex, WebRequest request) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Map<String, String> errors = new HashMap<>();

        constraintViolations.forEach(constraintViolation ->
                errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));

        log.info("Validation errors: {}", constraintViolations.toString());
        log.debug("Exception occurred: {}", ex);
        log.info("Response code = {}", HttpStatus.BAD_REQUEST);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ViolationErrorResponse
                        .builder()
                        .errors(errors)
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }


    @ExceptionHandler({
            AccessDeniedException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthenticationException ex, WebRequest request) {
        log.error(UNAUTHORIZED_ERROR_MESSAGE + " " + ex);
        log.info("Response code = {}", HttpStatus.UNAUTHORIZED);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("Exception occurred: {}", ex);
        log.info("Response code = {}", HttpStatus.NOT_FOUND);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.NOT_FOUND.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleNotRecognizedException(Exception ex, WebRequest request) {
        log.error("Exception occurred", ex);
        log.info("Response code = {}", HttpStatus.INTERNAL_SERVER_ERROR);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        System.out.println(ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse
                        .builder()
                        .message(ex.getMessage())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .path(((ServletWebRequest) request).getRequest().getRequestURI())
                        .build());
    }
}
