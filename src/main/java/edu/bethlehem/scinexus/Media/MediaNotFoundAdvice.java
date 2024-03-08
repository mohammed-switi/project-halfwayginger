package edu.bethlehem.scinexus.Media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MediaNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(MediaNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String mediaNotFoundHandler(MediaNotFoundException ex) {
    return ex.getMessage();
  }
}
