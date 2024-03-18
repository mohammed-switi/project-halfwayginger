package edu.bethlehem.scinexus.User;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("Could not find User" + id);
  }
  public UserNotFoundException(String message){
    super(message);
  }
}
