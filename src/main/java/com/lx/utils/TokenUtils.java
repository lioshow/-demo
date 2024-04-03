package com.lx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lx.entity.User;
import com.lx.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    private static UserMapper staticUserMapper;

    @Resource
    UserMapper userMapper;

    @PostConstruct
    public void setUserService()
    {
        staticUserMapper = userMapper;
    }

    /**
     * 生成token
     *
     * @return
     */
    public static String createToken(String phone, String sign)
    {
        return JWT.create().withAudience(phone) // 将phone保存到token里面，作为载荷
                .withExpiresAt(DateUtils.addHours(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以password作为token密钥
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return user对象
     */
    public static User getCurrentUser()
    {
        try
        {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(token))
            {
                String phone = JWT.decode(token).getAudience().get(0);
                return staticUserMapper.selectById(phone);
            }
        }
        catch (Exception e)
        {
            return null;
        }
        return null;
    }
}
