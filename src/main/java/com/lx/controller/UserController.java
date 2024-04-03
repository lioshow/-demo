package com.lx.controller;

import com.lx.annotation.AuthAccess;
import com.lx.entity.User;
import com.lx.pojo.Form;
import com.lx.pojo.RequestObject;
import com.lx.pojo.ResetForm;
import com.lx.service.IUserService;
import com.lx.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 刘肖
 * @since 2023-11-27
 */
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    private IUserService userService;

    @AuthAccess
    @PostMapping("/register")
    public R register(@RequestBody RequestObject request)
    {
        return userService.register(request);
    }

    @PostMapping("/login")
    public R login(@RequestBody Form form)
    {
        return userService.login(form);
    }

    @PutMapping("/reset")
    public R reset(@RequestBody ResetForm resetForm)
    {
        return userService.reset(resetForm);
    }

    @GetMapping("/sendEmailCode")
    public R sendEmailCode(String email)
    {
        return userService.sendEmailCode(email);
    }

    @GetMapping("/getUser")
    public R getUser(@RequestParam String phone)
    {
        return userService.getUser(phone);
    }

    @PutMapping("/update")
    public R update(@RequestBody User user)
    {
        return userService.update(user);
    }

    @GetMapping("/score")
    public R getScore(String idNo)
    {
        return userService.getScore(idNo);
    }
}