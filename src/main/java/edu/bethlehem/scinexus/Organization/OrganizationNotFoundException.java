package edu.bethlehem.scinexus.Organization;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class OrganizationNotFoundException extends GeneralException {

  public OrganizationNotFoundException(long id, HttpStatus httpStatus) {
    super("Organization With Id : " + id + ", Is Not Found", httpStatus);
  }

  public OrganizationNotFoundException(long id) {
    super("Organization With Id : " + id + ", Is Not Found", HttpStatus.NOT_FOUND);
  }

  public OrganizationNotFoundException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
