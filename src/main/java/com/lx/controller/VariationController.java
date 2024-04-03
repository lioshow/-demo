package com.lx.controller;


import com.lx.service.IVariationService;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-07
 */
@RestController
@RequestMapping("/variation")
public class VariationController {

    @Autowired
    private IVariationService variationService;

    @GetMapping("/getVariation")
    public R getVariation(@RequestParam String idNo)
    {
        return variationService.getVariation(idNo);
    }

    @PostMapping("/save")
    public R save(@RequestParam String idNo)
    {
        return variationService.save(idNo);
    }
}
