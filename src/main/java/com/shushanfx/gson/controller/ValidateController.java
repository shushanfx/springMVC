package com.shushanfx.gson.controller;

import com.shushanfx.gson.bean.ResultInfo;
import com.shushanfx.gson.validate.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dengjianxin on 2015/11/23.
 */
@Controller
@RequestMapping("/validation")
public class ValidateController {
    @Autowired
    private ValidateService validateService = null;

    @RequestMapping("/upload")
    public @ResponseBody
    ResultInfo upload(HttpServletRequest request, HttpServletResponse response){
        ResultInfo info = new ResultInfo();



        return info;
    }

    /**
     *
     * @param type
     * @param xml
     * @return
     */
    @RequestMapping("/validate")
    public @ResponseBody
    ResultInfo validate(Integer type, String xml){
        ResultInfo info = new ResultInfo();

        if(type == null){
            type = 1;
        }

        return info;
    }
}
