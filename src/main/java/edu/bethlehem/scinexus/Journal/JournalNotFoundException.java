package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Error.GeneralException;
import lombok.Getter;

import org.springframework.http.HttpStatus;

public class JournalNotFoundException extends GeneralException {

  public JournalNotFoundException(Long id, HttpStatus httpStatus) {
    super("Journal With Id : " + id + ", is Not Found", httpStatus);
  }

}
