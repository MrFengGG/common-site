package com.feng.home.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    Object handleException(Exception e){
        return FrontRequestResult.fail(e.getMessage());
    }
}
