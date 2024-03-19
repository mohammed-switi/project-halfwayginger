package edu.bethlehem.scinexus.Interaction;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class InteractionNotFoundException extends GeneralException {

  public InteractionNotFoundException(long id, HttpStatus httpStatus) {
    super("Interaction With Id : " + id + ", is Not Found ", httpStatus);
  }
}
