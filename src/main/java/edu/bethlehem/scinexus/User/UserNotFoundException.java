package edu.bethlehem.scinexus.User;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralException {

  public UserNotFoundException(String message, HttpStatus httpStatus){
    super(message,httpStatus);
  }

}
