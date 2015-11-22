package com.shushanfx.gson;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController{
    @RequestMapping(path = {"/index", "/", "/home"})
    public String index(){
        return "index";
    }

    @RequestMapping("/json")
    @ResponseBody
    public Map<String, Object> json(){
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("hello", "world");

        return map;
    }
}