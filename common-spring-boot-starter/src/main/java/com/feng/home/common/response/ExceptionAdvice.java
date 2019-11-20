package com.feng.home.common.response;

import com.feng.home.common.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    @ExceptionHandler(BusinessException.class)
    Object handleException(BusinessException e){
        return FrontRequestResult.fail(e.getMessage(), e.getResponseCode());
    }

}
