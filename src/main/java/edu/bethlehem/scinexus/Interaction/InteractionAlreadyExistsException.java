package edu.bethlehem.scinexus.Interaction;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class InteractionAlreadyExistsException extends GeneralException {

  public InteractionAlreadyExistsException(long id) {
    super(
        "Interaction with user: " + id + " already exists",
        HttpStatus.CONFLICT);
  }

  public InteractionAlreadyExistsException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
