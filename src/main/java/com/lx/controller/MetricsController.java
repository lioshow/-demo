package com.lx.controller;


import com.lx.service.IMetricsService;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-07
 */
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private IMetricsService metricsService;

    @GetMapping("/getMetrics")
    public R getMetrics(@RequestParam String idNo)
    {
        return metricsService.getMetrics(idNo);
    }
}
