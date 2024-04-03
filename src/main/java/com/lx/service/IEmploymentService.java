package com.lx.service;

import com.lx.entity.Employment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-28
 */
public interface IEmploymentService extends IService<Employment> {

    R update(Employment employment);
}
