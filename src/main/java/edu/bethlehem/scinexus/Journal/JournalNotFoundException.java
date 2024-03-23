package edu.bethlehem.scinexus.Journal;

import edu.bethlehem.scinexus.Error.GeneralException;
import org.springframework.http.HttpStatus;

public class JournalNotFoundException extends GeneralException {
    public JournalNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public JournalNotFoundException(String message) {
        super(message);
    }

    public JournalNotFoundException(Long id){
        super("Id : "+ id +", Journal is Not Found Exception",HttpStatus.NOT_FOUND);
    }
}
