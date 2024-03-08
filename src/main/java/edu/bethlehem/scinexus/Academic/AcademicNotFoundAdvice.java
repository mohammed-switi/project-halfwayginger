package edu.bethlehem.scinexus.Academic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AcademicNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(AcademicNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String academicNotFoundHandler(AcademicNotFoundException ex) {
    return ex.getMessage();
  }
}
