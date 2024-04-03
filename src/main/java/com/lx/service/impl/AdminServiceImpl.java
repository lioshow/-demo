package com.lx.service.impl;

import com.lx.entity.Admin;
import com.lx.mapper.AdminMapper;
import com.lx.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
