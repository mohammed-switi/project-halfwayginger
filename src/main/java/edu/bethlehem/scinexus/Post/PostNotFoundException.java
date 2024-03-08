package edu.bethlehem.scinexus.Post;

public class PostNotFoundException extends RuntimeException {

  public PostNotFoundException(Long id) {
    super("Could not find Post" + id);
  }
}
