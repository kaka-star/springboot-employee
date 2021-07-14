package com.glory.springbootemployee.config;

import com.glory.springbootemployee.component.LoginHandlerIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * @author: kaka
 * @date: 2021/7/14
 * @description:
 */
@Configuration
public class MyAppConfig
{
    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
       //注册拦截器
       WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
           @Override
           public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(new LoginHandlerIntercepter())
                       .addPathPatterns("/**")
                       .excludePathPatterns("/", "/index.html", "/user/login");
           }
       };
       return adapter;
    }
}
