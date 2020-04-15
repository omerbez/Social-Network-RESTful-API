package com.omer.socialapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice // ControllerAdvice + ResponseBody
public class ControllerAdvistor 
{
     @ExceptionHandler(value = {UserNotFoundException.class, PageNotFoundException.class, PostNotFoundException.class,
    		 GroupNotFoundException.class, CommentNotFoundException.class})
     public ResponseEntity<ErrorMessage> notFoundExceptionHandler(RuntimeException ex, WebRequest request) {
         return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
     }
     
     @ExceptionHandler(value = {GeneralException.class, IllegalArgumentException.class, HttpRequestMethodNotSupportedException.class})
     public ResponseEntity<ErrorMessage> specificExceptionHandler(Exception ex, WebRequest request) {
         return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
     }
     
     @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorMessage> generalExceptionHandler(Exception ex, WebRequest request) {
    	 ex.printStackTrace();
         return new ResponseEntity<>(new ErrorMessage("Something went wrong."), HttpStatus.BAD_REQUEST);
     }
}
