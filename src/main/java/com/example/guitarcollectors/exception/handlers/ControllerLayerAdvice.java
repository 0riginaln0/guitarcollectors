package com.example.guitarcollectors.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.guitarcollectors.exception.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerLayerAdvice {
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequestHandler(BadRequestException ex) {
        log.error("\n" + ex.getMessage()
                + "\n    file name: " + ex.getStackTrace()[0].getFileName()
                + "\n   class name: " + ex.getStackTrace()[0].getClassName()
                + "\n  method name: " + ex.getStackTrace()[0].getMethodName()
                + "\n  line number: " + ex.getStackTrace()[0].getLineNumber());
        return ex.getMessage();
    }
}
