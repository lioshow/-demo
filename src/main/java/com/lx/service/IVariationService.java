package com.lx.service;

import com.lx.entity.Variation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-07
 */
public interface IVariationService extends IService<Variation> {

    R getVariation(String idNo);

    R save(String idNo);
}
