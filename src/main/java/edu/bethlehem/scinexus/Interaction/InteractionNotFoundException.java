package edu.bethlehem.scinexus.Interaction;

public class InteractionNotFoundException extends RuntimeException {

  public InteractionNotFoundException(Long id) {
    super("Could not find Interaction" + id);
  }
}
