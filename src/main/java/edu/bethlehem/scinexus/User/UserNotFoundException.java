package edu.bethlehem.scinexus.User;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GeneralException {

  public UserNotFoundException(String message, HttpStatus httpStatus){
    super(message,httpStatus);
  }

  public UserNotFoundException(){

    super("User Not Found",HttpStatus.NOT_FOUND);
  }

  public UserNotFoundException(Long userId){

    super("ID : "+ userId+", User Not Found",HttpStatus.NOT_FOUND);

  }

  public UserNotFoundException(String message){
    super(message,HttpStatus.NOT_FOUND);
  }

}
