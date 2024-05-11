package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class PasswordDoesntMatchException extends GeneralException {

    public PasswordDoesntMatchException(){

        super("Password Doesn't Match", HttpStatus.UNAUTHORIZED);
    }

}
