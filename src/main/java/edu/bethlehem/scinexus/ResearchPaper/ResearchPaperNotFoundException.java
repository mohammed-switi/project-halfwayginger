package edu.bethlehem.scinexus.ResearchPaper;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class ResearchPaperNotFoundException extends GeneralException {

  public ResearchPaperNotFoundException(long id , HttpStatus httpStatus) {
    super("Research Paper With Id : " + id + ", is Not Found", httpStatus);
  }
}
