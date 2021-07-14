package com.glory.springbootemployee.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: kaka
 * @date: 2021/7/14
 * @description:
 */
public class LoginHandlerIntercepter implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        logger.info("logionUser="+loginUser);
        if(loginUser==null){
            //未登录，需要返回登录页面
            logger.info("logionUser=null");
            request.setAttribute("msg","没有权限 ，请先登录");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else{
            logger.info("loginUser!=null");
            return true;
        }
    }
}


