//package edu.bethlehem.scinexus.Academic;
//
//import edu.bethlehem.scinexus.Error.GeneralErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.util.Date;
//
//@ControllerAdvice
//public class AcademicNotFoundAdvice {
//
//  @ExceptionHandler(AcademicNotFoundException.class)
//  ResponseEntity<GeneralErrorResponse> academicNotFoundHandler(AcademicNotFoundException ex) {
//    GeneralErrorResponse errorResponse = new GeneralErrorResponse(
//            ex.getHttpStatus().value(),
//            ex.getMessage(),
//            new Date(System.currentTimeMillis())
//    );
//
//    return new ResponseEntity<>(errorResponse,ex.getHttpStatus());
//
//  }
//}
