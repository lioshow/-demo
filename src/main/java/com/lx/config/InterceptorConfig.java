//package com.lx.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport
//{
//    @Value("${file-save-path}")
//    String fileSavePath;
//
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry)
//    {
//        registry.addInterceptor(jwtInterceptor()) // 配置jwt的拦截规则
//                .addPathPatterns("/**") // 拦截请求所有路径
//                .excludePathPatterns("/user/login", "/user/register", "/images/**")  // 放行login和register
//                .excludePathPatterns(fileSavePath);
//        super.addInterceptors(registry);
//    }
//
//    @Bean
//    public JwtInterceptor jwtInterceptor()
//    {
//        return new JwtInterceptor();
//    }
//}
