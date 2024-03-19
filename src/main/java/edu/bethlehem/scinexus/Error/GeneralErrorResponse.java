package edu.bethlehem.scinexus.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GeneralErrorResponse {

    int status;
    String message;
   // Throwable throwable;
    Date timestamp;
}
