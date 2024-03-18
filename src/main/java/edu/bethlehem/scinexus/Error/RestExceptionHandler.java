package edu.bethlehem.scinexus.Error;


import edu.bethlehem.scinexus.Academic.AcademicNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(AcademicNotFoundException.class)
    ResponseEntity<GeneralErrorResponse> academicNotFoundHandler(AcademicNotFoundException ex) {
        GeneralErrorResponse errorResponse = new GeneralErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                new Date(System.currentTimeMillis())
        );

        return new ResponseEntity<>(errorResponse,ex.getHttpStatus());

    }

}
