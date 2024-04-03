package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lx.entity.User;
import com.lx.entity.Variation;
import com.lx.mapper.UserMapper;
import com.lx.mapper.VariationMapper;
import com.lx.service.IVariationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘肖
 * @since 2023-12-07
 */
@Service
public class VariationServiceImpl extends ServiceImpl<VariationMapper, Variation> implements IVariationService {

    @Autowired
    private VariationMapper variationMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public R getVariation(String idNo)
    {
        try
        {
            LambdaQueryWrapper<Variation> variationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            variationLambdaQueryWrapper.eq(Variation::getIdNo, idNo);
            LocalDateTime date = LocalDateTime.now();
            List<Variation> list = variationMapper.selectList(variationLambdaQueryWrapper);
            for(int i=0;i< list.size();i++)
            {
                Duration duration = Duration.between(date,list.get(i).getTime());
                long days = duration.toDays(); //相差的天数
                if(days > 15)
                {
                    list.remove(i);
                }
            }
            return R.success(list, "获取成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("获取失败");
        }
    }

    @Override
    public R save(String idNo)
    {
        try
        {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getIdNo, idNo);
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            Variation variation = new Variation(user.getIdNo(), user.getScore(), LocalDateTime.now());
            variationMapper.insert(variation);
            return R.success(variation, "保存成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("保存失败");
        }
    }
}
