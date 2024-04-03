package com.lx.exception;


import com.lx.utils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException
{

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public R serviceException(ServiceException e)
    {
        return R.error(e.getCode(), e.getMessage());
    }
}
