package edu.bethlehem.scinexus.ResearchPaper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ResearchPaperNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ResearchPaperNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String researchpaperNotFoundHandler(ResearchPaperNotFoundException ex) {
    return ex.getMessage();
  }
}
