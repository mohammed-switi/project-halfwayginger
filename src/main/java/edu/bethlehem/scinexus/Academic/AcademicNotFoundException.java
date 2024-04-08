package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.Error.GeneralException;

import org.springframework.http.HttpStatus;

public class AcademicNotFoundException extends GeneralException {

  public AcademicNotFoundException(Long id, HttpStatus httpStatus) {
    super("Academic With Id : " + id + ", is Not Found", httpStatus);
  }

  public AcademicNotFoundException(Long id) {
    super("Academic With Id : " + id + ", is Not Found", HttpStatus.NOT_FOUND);
  }

}
