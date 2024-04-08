package edu.bethlehem.scinexus.Error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class GeneralErrorResponse {

    int status;
    String message;
    Throwable throwable;
    Date timestamp;

    Map<String,String> validationError;


}
