package edu.bethlehem.scinexus.Opinion;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class OpinionNotFoundException extends GeneralException {

  public OpinionNotFoundException(long id, HttpStatus httpStatus) {
    super("Opinion With Id : " + id + ", is Not Found ", httpStatus);
  }

  public OpinionNotFoundException(String id, HttpStatus httpStatus) {
    super("Opinion With Id : " + id + ", is Not Found ", httpStatus);
  }
}
