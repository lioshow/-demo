package com.lx.dto;

import lombok.Data;

/**
 * @author 刘肖
 */
@Data
public class IdCardDataBackData {
    /**
     * 办理地址
     */
    private String issueAuthority;

    /**
     * 生效-到期
     */
    private String validPeriod;
}
