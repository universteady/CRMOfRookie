package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 罗健
 * 2021/4/27
 */
@Controller
public class DicIndexController {

    @RequestMapping("/settings/dictionary/index.do")
    public String dicIndex(){
        return "/settings/dictionary/index";
    }
}
