package edu.bethlehem.scinexus.Academic;

public class AcademicNotFoundException extends RuntimeException {

  public AcademicNotFoundException(Long id) {
    super("Could not find Academic" + id);
  }
}
