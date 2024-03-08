package edu.bethlehem.scinexus.Post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PostNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(PostNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String postNotFoundHandler(PostNotFoundException ex) {
    return ex.getMessage();
  }
}
