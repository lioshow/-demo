package com.lx.service.impl;

import com.lx.entity.Employment;
import com.lx.mapper.EmploymentMapper;
import com.lx.service.IEmploymentService;
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
 * @since 2023-11-28
 */
@Service
public class EmploymentServiceImpl extends ServiceImpl<EmploymentMapper, Employment> implements IEmploymentService {

    @Autowired
    private EmploymentMapper employmentMapper;

    @Override
    public R update(Employment employment)
    {
        try
        {
            employment.setModifyTimes(employment.getModifyTimes() + 1);
            employmentMapper.updateById(employment);
            return R.success(employment, "修改成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }
}
