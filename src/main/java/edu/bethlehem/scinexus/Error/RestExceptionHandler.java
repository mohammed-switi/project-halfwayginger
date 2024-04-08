package edu.bethlehem.scinexus.Error;

import edu.bethlehem.scinexus.Auth.Email.EmailException;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler {

        @ExceptionHandler(GeneralException.class)
        ResponseEntity<GeneralErrorResponse> restExceptionHandler(GeneralException ex) {

                GeneralErrorResponse errorResponse = GeneralErrorResponse
                                .builder()
                                .status(ex.getHttpStatus().value())
                                .message("This is Form the General Exception : " + ex.getMessage())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, ex.getHttpStatus());

        }

        @ExceptionHandler(UserNotFoundException.class)
        public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
                GeneralErrorResponse errorResponse = GeneralErrorResponse
                                .builder()
                                .status(ex.getHttpStatus().value())
                                .message(ex.getMessage())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, ex.getHttpStatus());

        }

        @ExceptionHandler(SignatureException.class)
        public ResponseEntity<String> handleSignatureException(SignatureException e) {
                Throwable cause = e.getCause();
                if (cause instanceof UnsupportedJwtException || cause instanceof MalformedJwtException) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body("Invalid JWT Token: " + cause.getMessage());
                } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Internal Server Error: " + cause.getMessage());
                }
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<GeneralErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();

                ex
                                .getBindingResult()
                                .getAllErrors()
                                .forEach((error) -> {
                                        String fieldName = ((FieldError) error).getField();
                                        String errorMessage = error.getDefaultMessage();
                                        errors.put(fieldName, errorMessage);
                                });
                GeneralErrorResponse errorResponse = GeneralErrorResponse
                                .builder()
                                .message("Vaildation Constraint Violation")
                                .status(HttpStatus.BAD_REQUEST.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .validationError(errors)
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex) {
                List<String> errors = new ArrayList<>();

                ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

                Map<String, List<String>> result = new HashMap<>();
                result.put("errors", errors);

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .validationError((Map<String, String>) errors).build();

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
                // You can customize the response entity as per your requirements
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This is happend From HttpMessageNotReadableException : The request body is invalid or unreadable."
                                                + "" + ex.getMessage() + " "
                                                + ex.getMostSpecificCause().toString())
                                .status(400)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException ex) {
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("Http Request Method Not Supported. " + ex.getMessage() + " "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAll(Exception ex) throws Exception {

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This handling from Handle all : An error occurred while processing the request. "
                                                + ex.getMessage() + "  "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(500)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        @ExceptionHandler(TransactionSystemException.class)
        public ResponseEntity<Object> handleTransactionException(TransactionSystemException ex) {
                // Customize your response here, for example:
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("This is happened from transitionSystem Exception An error occurred while processing the request. "
                                                + ex.getMostSpecificCause().getMessage()
                                                + "  "
                                                + ex.getStackTrace().toString())
                                .status(500)
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpMessageConversionException.class)
        public ResponseEntity<Object> handleHttpMessageConversionException(HttpMessageConversionException ex) {
                // Customize your response here, for example:

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message(" This is happeend form httpMessageConversion An error occurred while processing the request. "
                                                + ex.getMostSpecificCause().getMessage()
                                                + "  "
                                                + Arrays.toString(ex.getStackTrace()))
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<Object> handleResourceNotFoundException(NoResourceFoundException ex) {

                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .message("this is from NO Resoucre Found Exception" + ex.getMessage() + " "
                                                + ex.getBody().toString())
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(value = AccessDeniedException.class)
        public void handleConflict(HttpServletResponse response) throws IOException {
                response.sendError(403, "Your Message");
        }

        @ExceptionHandler(InternalAuthenticationServiceException.class)
        public ResponseEntity<Object> handleInternalAuthenticationServiceException(
                        InternalAuthenticationServiceException ex) {
                // Log the exception for troubleshooting

                // Customize your response here
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message("Internal authentication service error. " + ex.getMessage())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(ex.getCause())
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(EmailException.class)
        public ResponseEntity<Object> handleEmailException(EmailException exception) {
                GeneralErrorResponse errorResponse = GeneralErrorResponse.builder()
                                .message(exception.getMessage())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .timestamp(new Date(System.currentTimeMillis()))
                                .throwable(exception.getCause())
                                .build();

                return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
        }

}
