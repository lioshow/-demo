package com.lx.dto;


import lombok.Data;

@Data
public class OCRResponse
{
    private IdCardRoot idCardRoot;
    private String url;

    public OCRResponse(IdCardRoot idCardRoot, String url)
    {
        this.idCardRoot = idCardRoot;
        this.url = url;
    }
}
