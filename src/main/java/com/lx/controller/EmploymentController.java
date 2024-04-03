package com.lx.controller;


import com.lx.entity.Employment;
import com.lx.service.IEmploymentService;
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
@RequestMapping("/employment")
public class EmploymentController {

    @Autowired
    private IEmploymentService employmentService;

    @PutMapping("/update")
    public R update(@RequestBody Employment employment)
    {
        return employmentService.update(employment);
    }
}
