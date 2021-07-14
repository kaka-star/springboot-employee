package com.glory.springbootemployee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: kaka
 * @date: 2021/6/27
 * @description:
 */
@Controller
public class LoginController {
//    @ResponseBody
    @PostMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Map<String, Object> map, HttpSession session) {
        if(!StringUtils.isEmpty(username)&&!StringUtils.isEmpty(password)&&username.equals("admin")&&password.equals("123")){
            session.setAttribute("loginUser", username);
            return "dashboard";
        }else{
            map.put("msg","用户名或者密码错误");
            return "index";
        }
    }

    @GetMapping("/log_out")
    public String logout(HttpSession session){
        //退出成功，删除session中的用户信息
        session.removeAttribute("loginUser");
        return "index";
    }
}
