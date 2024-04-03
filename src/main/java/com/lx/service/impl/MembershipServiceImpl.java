package com.lx.service.impl;

import com.lx.entity.Membership;
import com.lx.mapper.MembershipMapper;
import com.lx.service.IMembershipService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, Membership> implements IMembershipService {

}
