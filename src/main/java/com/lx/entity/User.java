package com.lx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "phone")
    private String phone;

    @TableField("idNo")
    private String idNo;

    private String password;

    private String email;

    private Integer score;

    private Integer days;

    @TableField("modifyTimes")
    private Integer modifyTimes;

    // 用户token
    @TableField(exist = false)
    private String token;

    public User(String idNo, String phone, String password, String email)
    {
        this.idNo = idNo;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.score = 0;
        this.days = 0;
        this.modifyTimes = 0;
    }

    public User(){}
}
