package com.lx.exception;


import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class ServiceException extends RuntimeException
{
    private Integer code;

    public ServiceException(Integer code, String msg)
    {
        super(msg);
        this.code = code;
    }
}
