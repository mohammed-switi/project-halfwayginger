package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends GeneralException {

    public UserNotAuthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }

    public UserNotAuthorizedException(Long id){
        super("User Id : "+id+", Is Not Authorized", HttpStatus.UNAUTHORIZED);
    }

    public UserNotAuthorizedException(){
        super("User is Not Authorized", HttpStatus.UNAUTHORIZED);
    }
}
