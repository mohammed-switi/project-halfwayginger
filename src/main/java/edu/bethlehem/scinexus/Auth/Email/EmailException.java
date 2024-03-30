package edu.bethlehem.scinexus.Auth.Email;

import edu.bethlehem.scinexus.Error.GeneralException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailException extends GeneralException {

    Throwable customCause;
    public EmailException(String failedToSendEmail,HttpStatus httpStatus) {
        super(failedToSendEmail,httpStatus);
    }

    public EmailException(String failedToSendEmail,HttpStatus httpStatus, Throwable cause) {
        super(failedToSendEmail,httpStatus);
        this.customCause=cause;
    }

}
