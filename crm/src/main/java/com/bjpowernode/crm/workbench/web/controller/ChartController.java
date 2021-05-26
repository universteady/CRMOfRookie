package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 罗健
 * 2021/5/16
 */
@Controller
public class ChartController {
    @Autowired
    private TranService tranService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String tranIndex(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage(){
        List<FunnelVO> funnelVOList = tranService.queryCountOfTranGroupByStage();
        return funnelVOList;
    }

    @RequestMapping("/workbench/chart/activity/index.do")
    public String activityIndex(){
        return "workbench/chart/activity/index";
    }

    @RequestMapping("/workbench/chart/activity/queryCostOfActivity.do")
    @ResponseBody
    public Object queryCostOfActivity(){
        return activityService.queryCostOfActivity();
    }
}
