package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 罗健
 * 2021/5/9
 */
@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(Model model){
        //提供所有用户列表
        List<User> userList = userService.queryAllUsers();
        //提供所有称呼列表
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        //提供所有线索状态列表
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        //提供所有线索来源列表
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");


        model.addAttribute("userList", userList);
        model.addAttribute("appellationList", appellationList);
        model.addAttribute("clueStateList", clueStateList);
        model.addAttribute("sourceList", sourceList);

        return "/workbench/clue/index";
    }

    @RequestMapping("workbench/clue/queryClueForPageByCondition.do")
    @ResponseBody
    public Object queryClueForPageByCondition(int pageNo, int pageSize, String fullName, String company, String mphone, String phone,String source,String owner,String state){
        Map<String, Object> map = new HashMap<>();
        int beginNo = (pageNo - 1) * pageSize;
        map.put("beginNo", beginNo);
        map.put("pageSize", pageSize);
        map.put("fullName", fullName);
        map.put("company", company);
        map.put("mphone", mphone);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("state", state);


        //获取满足条件的所有的市场活动
        List<Clue> clueList = clueService.queryClueForPageByCondition(map);
        //获取满足条件的市场活动条数
        long totalRows = clueService.queryCountOfClueByCondition(map);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("clueList", clueList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    @RequestMapping("workbench/clue/saveCreateClue.do")
    @ResponseBody
    public ReturnObject saveCreateClue(Clue clue, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        int ret = 0;

        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = clueService.saveCreateClue(clue);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加市场活动失败");
            }
            return returnObject;
        }
    }

    @RequestMapping("workbench/clue/editClue.do")
    @ResponseBody
    public Clue editClue(String id) {
        Clue clue = clueService.queryClueById(id);
        return clue;
    }

    @RequestMapping("workbench/clue/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        int ret = 0;

        User user = (User) session.getAttribute(Constant.SESSION_USER);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = clueService.updateByPrimaryKeySelective(clue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新市场活动失败");
            }

            return returnObject;
        }
    }

    @RequestMapping("workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.deleteClueByIds(id);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除线索失败");
        }
        return returnObject;
    }

    @RequestMapping("workbench/clue/detailClue.do")
    public String detailClue(String id,Model model){
        //线索详情
        Clue clue=clueService.queryClueForDetailById(id);
        //线索备注
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);

        //与该线索相关联的市场活动
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);

        model.addAttribute("clue", clue);
        model.addAttribute("remarkList", remarkList);
        model.addAttribute("activityList",activityList);

        return "workbench/clue/detail";

    }

    //关联与解除关联市场活动

    //查询未关联市场活动
    @RequestMapping("workbench/clue/searchUnbundActivity.do")
    @ResponseBody
    public Object searchUnbundActivity(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        List<Activity> activityList = activityService.queryActivityNoBoundByClueId(map);
        return activityList;
    }

    //关联市场活动
    @RequestMapping("workbench/clue/saveBundActivity.do")
    @ResponseBody
    public Object saveBundActivity(String clueId,String[] activityId){
        ClueActivityRelation clueActivityRelation = null;
        List<ClueActivityRelation> list = new ArrayList<>();
        ReturnObject returnObject = new ReturnObject();

        for (int i = 0; i < activityId.length; i++) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(activityId[i]);

            list.add(clueActivityRelation);
        }

        try {
            int num = clueActivityRelationService.saveCreateClueActivityRelationByList(list);
            if(num>0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("关联市场活动失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("关联市场活动失败");
        }
        return returnObject;
    }

    //解除关联市场活动
    @RequestMapping("workbench/clue/saveUnbundActivity.do")
    @ResponseBody
    public Object saveUnbundActivity(ClueActivityRelation relation){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if(ret>0){
               returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("解除市场活动关联失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("解除市场活动关联失败");
        }
        return returnObject;
    }

    //跳转到线索转换页面
    @RequestMapping("/workbench/clue/convertClue.do")
    public String toConvertClue(String id,Model model){
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        model.addAttribute("clue", clue);
        model.addAttribute("stageList", stageList);

        return "workbench/clue/convert";
    }

    //根据名字模糊查询市场活动
    @RequestMapping("/workbench/clue/queryActivityForDetailByName.do")
    @ResponseBody
    public Object queryActivityForDetailByName(String activityName){
        List<Activity> activityList = activityService.queryActivityForDetailByName(activityName);
        return activityList;
    }

    //转换线索
    @RequestMapping("workbench/clue/saveConvertClue.do")
    @ResponseBody
    public Object saveConvertClue(String clueId,String isCreateTran,String amountOfMoney,String tradeName,String expectedClosingDate,String stage,String activityId,HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        Map<String,Object> map = new HashMap<>();
        ReturnObject returnObject = new ReturnObject();

        map.put("clueId", clueId);
        map.put("isCreateTran", isCreateTran);
        map.put("amountOfMoney", amountOfMoney);
        map.put("tradeName", tradeName);
        map.put("expectedClosingDate", expectedClosingDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("user", user);

        try {
            clueService.saveConvert(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("线索转换失败");
        }

        return returnObject;
    }

}
