package com.lx.service.impl;

import com.lx.entity.Education;
import com.lx.mapper.EducationMapper;
import com.lx.service.IEducationService;
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
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education> implements IEducationService {

    @Autowired
    private EducationMapper educationMapper;

    @Override
    public R update(Education education)
    {
        try
        {
            if (!(education.getDegree().equals("本科") || education.getDegree().equals("研究生")))
            {
                education.setTitle("");
            }
            education.setModifyTimes(education.getModifyTimes() + 1);
            educationMapper.updateById(education);
            return R.success(education, "修改成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("修改失败");
        }
    }
}
