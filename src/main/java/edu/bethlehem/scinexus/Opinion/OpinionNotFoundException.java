package edu.bethlehem.scinexus.Opinion;

public class OpinionNotFoundException extends RuntimeException {

  public OpinionNotFoundException(Long id) {
    super("Could not find Opinion" + id);
  }
}
