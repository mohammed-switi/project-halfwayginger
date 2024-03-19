package edu.bethlehem.scinexus.Error;


import edu.bethlehem.scinexus.User.UserNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {


//    @ExceptionHandler(AcademicNotFoundException.class)
//    ResponseEntity<GeneralErrorResponse> academicNotFoundHandler(AcademicNotFoundException ex) {
//        GeneralErrorResponse errorResponse = new GeneralErrorResponse(
//                ex.getHttpStatus().value(),
//                ex.getMessage(),
//                new Date(System.currentTimeMillis())
//        );
//
//        return new ResponseEntity<>(errorResponse,ex.getHttpStatus());
//
//    }

    @ExceptionHandler(GeneralException.class)
    ResponseEntity<GeneralErrorResponse> restExceptionHandler(GeneralException ex) {
        GeneralErrorResponse errorResponse = new GeneralErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
      //          ex,
                new Date(System.currentTimeMillis())
        );

        return new ResponseEntity<>(errorResponse,ex.getHttpStatus());

    }
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
            GeneralErrorResponse errorResponse = new GeneralErrorResponse(
                    ex.getHttpStatus().value(),
                    ex.getMessage(),
    //                ex,
                    new Date(System.currentTimeMillis())
            );
            return new ResponseEntity<>(errorResponse,ex.getHttpStatus());

        }
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }



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


}
