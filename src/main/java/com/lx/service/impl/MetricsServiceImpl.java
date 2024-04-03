package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lx.entity.Metrics;
import com.lx.mapper.MetricsMapper;
import com.lx.service.IMetricsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-07
 */
@Service
public class MetricsServiceImpl extends ServiceImpl<MetricsMapper, Metrics> implements IMetricsService {

    @Autowired
    private MetricsMapper metricsMapper;

    @Override
    public R getMetrics(String idNo)
    {
        try
        {
            LambdaQueryWrapper<Metrics> metricsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            metricsLambdaQueryWrapper.eq(Metrics::getIdNo, idNo);
            Metrics metrics = metricsMapper.selectOne(metricsLambdaQueryWrapper);
            if (metrics != null)
            {
                return R.success(metrics, "获取成功");
            }
            else
            {
                return R.error("用户不存在");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }
}
