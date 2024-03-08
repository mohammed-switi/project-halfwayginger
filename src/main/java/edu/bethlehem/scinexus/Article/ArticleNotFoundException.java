package edu.bethlehem.scinexus.Article;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(Long id) {
    super("Could not find Article" + id);
  }
}
