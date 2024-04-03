package com.lx.controller;


import com.lx.entity.Education;
import com.lx.service.IEducationService;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-28
 */
@RestController
@RequestMapping("/education")
public class EducationController {

    @Autowired
    private IEducationService educationService;

    @PutMapping("/update")
    public R update(@RequestBody Education education)
    {
        return educationService.update(education);
    }
}
