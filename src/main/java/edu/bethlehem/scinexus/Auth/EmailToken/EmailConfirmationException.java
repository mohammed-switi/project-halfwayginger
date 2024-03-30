package edu.bethlehem.scinexus.Auth.EmailToken;

import edu.bethlehem.scinexus.Error.GeneralErrorResponse;
import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

public class EmailConfirmationException extends GeneralException {

    public EmailConfirmationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public EmailConfirmationException(String message) {
        super(message);
    }
}

