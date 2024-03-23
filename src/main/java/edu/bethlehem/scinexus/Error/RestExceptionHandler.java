package edu.bethlehem.scinexus.Error;


import edu.bethlehem.scinexus.User.UserNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class RestExceptionHandler {



    @ExceptionHandler(GeneralException.class)
    ResponseEntity<GeneralErrorResponse> restExceptionHandler(GeneralException ex) {

        GeneralErrorResponse errorResponse= GeneralErrorResponse
                                                .builder()
                                                .status(ex.getHttpStatus().value())
                                                .message(ex.getMessage())
                                                .timestamp(new Date(System.currentTimeMillis()))
                                                .build();

        return new ResponseEntity<>(errorResponse,ex.getHttpStatus());

    }
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
            GeneralErrorResponse errorResponse = GeneralErrorResponse
                                                .builder()
                                                .status(ex.getHttpStatus().value())
                                                .message(ex.getMessage())
                                                .timestamp(new Date(System.currentTimeMillis()))
                                                .build();
            return new ResponseEntity<>(errorResponse,ex.getHttpStatus());

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
                                     }
                         );
                GeneralErrorResponse errorResponse=GeneralErrorResponse
                        .builder()
                        .message("Vaildation Constraint Violation")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(new Date(System.currentTimeMillis()))
                        .validationError(errors)
                        .build();

        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

        Map<String, List<String>> result = new HashMap<>();

        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        // You can customize the response entity as per your requirements
        GeneralErrorResponse errorResponse=GeneralErrorResponse.builder()
                .message("The request body is invalid or unreadable." + "" + ex.getMessage() +" " + ex.getMostSpecificCause().toString())
                .status(400)
                .timestamp(new Date(System.currentTimeMillis()))
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        // Customize your response here, for example:
        String message = "Method not supported";
        return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

}
