package com.musicStore.api_loja_discos.handler;

import com.musicStore.api_loja_discos.exceptions.*;
import com.musicStore.api_loja_discos.requests.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDetails> handlerBadRequestException(BadRequestException bre) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title(" Bad Request Exception, Check the Documentation")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ExceptionDetails> handlerRuleException(BusinessRuleException bre) {
        return new ResponseEntity<>(
                BusinessRuleExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .title("Business Rule Violation")
                        .details(bre.getMessage())
                        .developerMessage(bre.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReleaseYearException.class)
    public ResponseEntity<ExceptionDetails> handlerReleaseYearException(InvalidReleaseYearException rye) {
        return new ResponseEntity<>(
                InvalidReleaseYearExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Business Rule Violation")
                        .details(rye.getMessage())
                        .developerMessage(rye.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                                       HttpStatusCode status, WebRequest request) {

    List<FieldError> fieldErrors = ex.getFieldErrors();

        List<String> fields = fieldErrors.stream().map(FieldError::getField).toList();

        List<String> list = fieldErrors.stream().map(FieldError::getDefaultMessage).toList();


        return new ResponseEntity<>(
      ValidationExceptionDetails.builder()
              .timestamp(LocalDateTime.now())
              .status(HttpStatus.BAD_REQUEST.value())
              .title("Bad request Exception, Invalid Fields")
              .details("Check the field's error")
              .developerMessage(ex.getClass().getName())
              .fieldsError(fields)
              .fieldsMessage(list)
              .build(), headers, status);
    }

    @ExceptionHandler(InvalidArtistDataException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerInvalidArtistDataException(InvalidArtistDataException ex) {

        List<String> fieldsErrors = ex.getFields();

        List<String> message = ex.getFieldsMessage();


        return new ResponseEntity<>(ValidationExceptionDetails.builder()
             .timestamp(LocalDateTime.now())
             .status(HttpStatus.BAD_REQUEST.value())
             .title("Bad request Exception, Invalid Fields")
             .details("Check the field's error")
             .developerMessage(ex.getClass().getName())
             .fieldsError(fieldsErrors)
                .fieldsMessage(message)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(statusCode.value())
                .title("Bad request Exception, Invalid Fields")
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        return new ResponseEntity<>(exceptionDetails, headers, statusCode);
        }




        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ExceptionDetails> handlerResourceNotFound(ResourceNotFoundException rnt) {
            return new ResponseEntity<>(ResourceNotFoundExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Business Rule Violation")
                    .details(rnt.getMessage())
                    .developerMessage(rnt.getClass().getName())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ServiceInvocationException.class)
        public ResponseEntity<ExceptionDetails> handlerServiceInvocation(ServiceInvocationException sie) {
            return new ResponseEntity<>(ServiceInvocationExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Business Rule Violation")
                    .details(sie.getMessage())
                    .developerMessage(sie.getClass().getName())
                    .build(), HttpStatus.BAD_REQUEST);
        }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorResponseDTO errorOutput = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                "Requisition body is absent or invalid format",
                LocalDateTime.now()
        );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorOutput);

    }
}
