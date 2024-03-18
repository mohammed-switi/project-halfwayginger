package edu.bethlehem.scinexus.Notification;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends GeneralException {


  public NotificationNotFoundException(long id, HttpStatus httpStatus) {
    super("Notification with Id : " + id + ", is Not Found", httpStatus);
  }
}
