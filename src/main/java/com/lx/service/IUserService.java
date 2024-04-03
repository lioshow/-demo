package com.lx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.entity.User;
import com.lx.pojo.Form;
import com.lx.pojo.RequestObject;
import com.lx.pojo.ResetForm;
import com.lx.utils.R;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-28
 */
public interface IUserService extends IService<User> {

    R sendEmailCode(String email);

    R register(RequestObject request);

    R login(Form form);

    R reset(ResetForm resetForm);

    R saveUser(User user);

    R getUser(String phone);

    R update(User user);

    R getScore(String idNo);
}
