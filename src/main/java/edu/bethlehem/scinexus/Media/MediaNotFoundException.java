package edu.bethlehem.scinexus.Media;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class MediaNotFoundException extends GeneralException {

  public MediaNotFoundException(long id, HttpStatus httpStatus) {
    super("Media With Id : "+ id + ", is Not Found", httpStatus);
  }
}
