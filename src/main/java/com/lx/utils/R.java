package com.lx.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//通用返回结果类，将数据封装成类返回给前端

@Data
public class R<T>
{
    // 编码，1为成功，0为失败
    private Integer code;

    // 错误信息
    private String msg;

    // 数据
    private T data;

    public R() {}

    public R(Integer code, String msg, T data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功结果集
    public static <T> R<T> success(T object, String msg)
    {
        return new R<T>(200,msg, object);
    }

    // 失败结果集
    public static <T> R<T> error(Integer code, String msg)
    {
        return new R<T>(code,msg, null);
    }

    public static <T> R<T> error(String msg) { return new R<T>(0, msg,null); }
}
