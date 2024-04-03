package com.lx.pojo;


import com.lx.dto.IdCardDataType;
import com.lx.entity.Education;
import com.lx.entity.Employment;
import lombok.Data;

@Data
public class RequestObject {
    private Form registerForm;
    private IdCardDataType uploadData;
    private Education education;
    private Employment employment;
}
