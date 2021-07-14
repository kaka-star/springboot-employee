package com.glory.springbootemployee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Map;

/**
 * @author: kaka
 * @date: 2021/6/27
 * @description:
 */
@Controller
public class SuccessController {
    @RequestMapping("/success")
    public String success(Map<String, Object> map){
        map.put("k1", "hello");
        map.put("users", Arrays.asList("Lily","Cindy","Tom","Jim"));
        return  "success";
    }

    @RequestMapping({"/", "/index.html"})
    public String index(){
        return  "index";
    }

    @RequestMapping("/list")
    public String login(){
        return  "list";
    }

}
