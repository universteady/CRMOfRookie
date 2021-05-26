package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 罗健
 * 2021/4/25
 */
@Controller
public class workbenchController {

    @RequestMapping("workbench/index.do")
    public String toindex(){
        return "/workbench/index";
    }

    @RequestMapping("workbench/main/index.do")
    public String mainIndex(){
        return "workbench/main/index";
    }


}
