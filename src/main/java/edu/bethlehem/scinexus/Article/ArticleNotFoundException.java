package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class ArticleNotFoundException extends GeneralException {


  public ArticleNotFoundException(Long id, HttpStatus httpStatus) {
    super("Article With Id : " + id + ", is Not Found", httpStatus);
  }
}
