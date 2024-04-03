package com.lx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResult
{
    private String phone;
    private String token;
    private Integer days;
}
