//package com.unipi.giguniverse.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.persistence.EntityNotFoundException;
//
//@ControllerAdvice
//public class RestExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    protected ResponseEntity<Object> handleErrorMessage(String exMessage) {
//        ApplicationException applicationException = new ApplicationException("test");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exMessage);
//    }
//}
