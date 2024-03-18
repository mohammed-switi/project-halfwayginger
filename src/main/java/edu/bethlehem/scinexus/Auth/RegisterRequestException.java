package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class RegisterRequestException extends GeneralException {


    public RegisterRequestException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
