package edu.bethlehem.scinexus.Academic;

import org.springframework.http.HttpStatus;

public class AcademicNotFoundException extends RuntimeException {

  private HttpStatus httpStatus;

  public AcademicNotFoundException(Long id, HttpStatus httpStatus) {
    super("Could not find Academic with Id: " + id);

    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
