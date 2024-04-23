package com.favor.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CQ
 * @version 1.0
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域访问的路径：通配符 /** 表示所有路径
        registry.addMapping("/api/**")
                // 允许跨域访问的域名："*" 表示允许所有域名
                .allowedOrigins("*")
                // 允许的请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 预检请求的有效期，单位秒
                .maxAge(3600);
    }
}
