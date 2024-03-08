package edu.bethlehem.scinexus.Media;

public class MediaNotFoundException extends RuntimeException {

  public MediaNotFoundException(Long id) {
    super("Could not find Media" + id);
  }
}
