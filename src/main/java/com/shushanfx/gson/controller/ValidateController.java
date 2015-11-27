package com.shushanfx.gson.controller;

import com.shushanfx.gson.bean.Pagination;
import com.shushanfx.gson.bean.ResultInfo;
import com.shushanfx.gson.validate.ValidateService;
import com.shushanfx.gson.validate.ValidateTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * Created by dengjianxin on 2015/11/23.
 */
@Controller
@RequestMapping("/validation")
public class ValidateController {
    @Autowired
    private ValidateService validateService = null;

    @RequestMapping("/index")
    public String index(String name, Integer pageIndex, Integer pageSize, Model model){
        List<ValidateTemplate> list = validateService.listTemplate(name);
        Pagination<ValidateTemplate> pagination = new Pagination<ValidateTemplate>(pageIndex, pageSize, list);
        model.addAttribute("list", pagination.realList());
        model.addAttribute("pagination", pagination);
        return "validation/index";
    }

    @RequestMapping("/upload")
    public @ResponseBody
    ResultInfo upload(HttpServletRequest request){
        return validateService.upload(request);
    }

    @RequestMapping("/preUpload")
    public String preUpload(){
        return "/validation/preUpload";
    }

    @RequestMapping("/preValidate")
    public String preValidate(){
        return "validation/preValidate";
    }

    /**
     *
     * @param type
     * <ul>
     *     <li>1 for validate xml content, parameter xml is the content of xml.</li>
     *     <li>2 for validate xml file, parameter xml is a file's name.</li>
     *     <li>3 for validate xml url, parameter xml is a url.</li>
     * </ul>
     * @param xml
     * @return
     */
    @RequestMapping("/validate")
    public @ResponseBody
    ResultInfo validate(Integer type, String templateID, String xml){
        if(type == null){
            type = 1;
        }
        return validateService.validate(templateID, type, xml);
    }
}
