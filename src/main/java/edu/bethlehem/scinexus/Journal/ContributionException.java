package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ContributionException extends GeneralException {
    public ContributionException(String message) {
        super(message,HttpStatus.CONFLICT);
    }
}