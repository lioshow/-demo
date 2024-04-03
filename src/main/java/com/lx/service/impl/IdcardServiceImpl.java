package com.lx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lx.entity.Idcard;
import com.lx.mapper.IdcardMapper;
import com.lx.service.IIdcardService;
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
public class IdcardServiceImpl extends ServiceImpl<IdcardMapper, Idcard> implements IIdcardService {

    @Autowired
    private IdcardMapper idcardMapper;

    @Override
    public R saveIdCard(Idcard idcard)
    {
        try
        {
            QueryWrapper<Idcard> queryIdcard = new QueryWrapper<>();
            queryIdcard.eq("IdNo", idcard.getIdNo());
            Idcard i = idcardMapper.selectOne(queryIdcard);
            if (i == null)
            {
                return R.success(idcard, "注册成功");
            }
            else
            {
                return R.error("注册失败，身份证号已存在");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return R.error("注册失败");
        }
    }

}
