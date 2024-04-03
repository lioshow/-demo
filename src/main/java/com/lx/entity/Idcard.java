package com.lx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Idcard对象", description="")
public class Idcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "idNo")
    private String idNo;

    private String name;

    private String gender;

    private String ethnicity;

    @TableField("birthDate")
    private String birthDate;

    private String address;

    @TableField("modifyTimes")
    private Integer modifyTimes;

    public Idcard (String idNo, String name, String gender, String ethnicity, String birthDate, String address)
    {
        this.idNo = idNo;
        this.name = name;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.birthDate = birthDate;
        this.address = address;
        this.modifyTimes = 0;
    }
}
