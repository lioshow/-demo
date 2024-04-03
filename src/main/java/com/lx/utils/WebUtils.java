package com.lx.utils;

import javax.servlet.http.HttpServletResponse;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     *
     * @param response  渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(string);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
