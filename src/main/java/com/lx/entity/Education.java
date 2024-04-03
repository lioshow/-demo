package com.lx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="Education对象", description="")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "idNo")
    private String idNo;

    private String degree;

    private String title;

    private String school;

    @TableField("modifyTimes")
    private Integer modifyTimes;
}
