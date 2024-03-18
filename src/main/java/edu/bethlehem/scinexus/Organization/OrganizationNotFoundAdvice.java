//package edu.bethlehem.scinexus.Organization;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class OrganizationNotFoundAdvice {
//
//  @ResponseBody
//  @ExceptionHandler(OrganizationNotFoundException.class)
//  @ResponseStatus(HttpStatus.NOT_FOUND)
//  String organizationNotFoundHandler(OrganizationNotFoundException ex) {
//    return ex.getMessage();
//  }
//}
