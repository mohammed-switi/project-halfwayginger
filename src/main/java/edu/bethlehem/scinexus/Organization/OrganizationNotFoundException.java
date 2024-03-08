package edu.bethlehem.scinexus.Organization;

public class OrganizationNotFoundException extends RuntimeException {

  public OrganizationNotFoundException(Long id) {
    super("Could not find Organization" + id);
  }
}
