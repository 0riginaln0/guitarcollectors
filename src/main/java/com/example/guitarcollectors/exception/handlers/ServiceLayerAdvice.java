package com.example.guitarcollectors.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.guitarcollectors.exception.ForbiddenRequestException;
import com.example.guitarcollectors.exception.MyEntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ServiceLayerAdvice {
    @ResponseBody
    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String forbiddenRequestHandler(ForbiddenRequestException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MyEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String myEntityNotFoundHandler(MyEntityNotFoundException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }
}
