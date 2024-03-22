package edu.bethlehem.scinexus.Post;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;


public class PostNotFoundException extends GeneralException {

  public PostNotFoundException(long id, HttpStatus httpStatus) {
    super("Post With Id : " + id + ", is Not Found ", httpStatus);
  }


  public PostNotFoundException(long id) {
    super("Post With Id : " + id + ", is Not Found ", HttpStatus.NOT_FOUND);
  }
}
