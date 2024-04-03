package com.lx.service;

import com.lx.entity.Metrics;
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
public interface IMetricsService extends IService<Metrics> {

    R getMetrics(String idNo);
}
