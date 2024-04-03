package com.lx.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lx.annotation.AuthAccess;
import com.lx.entity.User;
import com.lx.exception.ServiceException;
import com.lx.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor
{
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token))
        {
            token = request.getParameter("token");
        }
        // 如果不是映射到方法直接跳过
        if (handler instanceof HandlerMethod)
        {
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null)
            {
                return true;
            }
        }

        // 执行认证
        if (StringUtils.isBlank(token))
        {
            throw new ServiceException(401, "请登录");
        }

        // 获取token中的user phone
        String phone;
        try
        {
            phone = JWT.decode(token).getAudience().get(0);
        }
        catch (JWTDecodeException j)
        {
            throw new ServiceException(401, "请登录");
        }

        // 根据token中的user phone查询数据库
        User user = userMapper.selectById(phone);
        if (user == null)
        {
            throw new ServiceException(401, "请登录");
        }

        // 用户密码加签验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try
        {
            jwtVerifier.verify(token);
        }
        catch (JWTVerificationException e)
        {
            throw new ServiceException(401, "请登录");
        }
        return true;
    }
}
