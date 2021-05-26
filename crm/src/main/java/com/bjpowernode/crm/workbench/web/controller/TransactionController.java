package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 罗健
 * 2021/5/14
 */
@Controller
public class TransactionController {

    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private TranService tranService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(Model model) {

        //提供所有交易阶段
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //提供所有交易类型
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        //提供所有交易来源
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");


        model.addAttribute("stageList", stageList);
        model.addAttribute("transactionTypeList", transactionTypeList);
        model.addAttribute("sourceList", sourceList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/createTran.do")
    public String createTran(Model model) {
        //提供所有的用户
        List<User> userList = userService.queryAllUsers();
        //提供所有交易阶段
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //提供所有交易类型
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        //提供所有交易来源
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        model.addAttribute("userList", userList);
        model.addAttribute("stageList", stageList);
        model.addAttribute("transactionTypeList", transactionTypeList);
        model.addAttribute("sourceList", sourceList);
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/queryTranForPageByCondition.do")
    @ResponseBody
    public Object queryTranForPageByCondition(int pageNo, int pageSize, String owner, String name, String customer, String stage,String type,String source,String contacts){
        Map<String, Object> map = new HashMap<>();
        int beginNo = (pageNo - 1) * pageSize;
        map.put("beginNo", beginNo);
        map.put("pageSize", pageSize);
        map.put("owner", owner);
        map.put("name", name);
        map.put("customer", customer);
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contacts", contacts);


        //获取满足条件的所有的交易
        List<Tran> tranList = tranService.queryTranForPageByCondition(map);
        //获取满足条件的交易条数
        long totalRows = tranService.queryCountOfTranByCondition(map);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("tranList", tranList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    //自动补全客户名称
    @RequestMapping("/workbench/transaction/queryCustomerByName.do")
    @ResponseBody
    public Object queryCustomerByName(String customerName){
        List<Customer> customerList = customerService.queryCustomerByName(customerName);
        return customerList;
    }

    //由阶段匹配可能性
    @RequestMapping("/workbench/transaction/getPossibilityByStageValue.do")
    @ResponseBody
    public Object getPossibilityByStageValue(String stageValue){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        return possibility;
    }

    //查询所有的联系人
    @RequestMapping("/workbench/transaction/queryContactsForDetailByName.do")
    @ResponseBody
    public Object queryContactsForDetailByName(String fullName){
        List<Contacts> contactsList = contactsService.queryContactsForDetailByName(fullName);
        return contactsList;
    }

    //保存交易
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(Tran tran, String customerName, HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map = new HashMap<>();
        map.put("tran", tran);
        map.put("customerName", customerName);
        map.put("sessionUser", user);

        ReturnObject returnObject = new ReturnObject();

        try {
            tranService.saveCreateTran(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("新增交易失败");
        }

        return  returnObject;
    }

    //跳转到detail页面
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,Model model){
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryForDetailByTranId(id);

        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        tran.setPossibility(possibility);

        //获取交易历史最后阶段的阶段编号
        String theOrderNo = null;

        if (tranHistoryList != null && tranHistoryList.size()>0) {
            TranHistory tranHistory = tranHistoryList.get(tranHistoryList.size()-1);
            theOrderNo = tranHistory.getOrderNo();
        }

        model.addAttribute("stageList", stageList);
        model.addAttribute("tran", tran);
        model.addAttribute("remarkList", remarkList);
        model.addAttribute("tranHistoryList", tranHistoryList);
        model.addAttribute("theOrderNo", theOrderNo);

        return "workbench/transaction/detail";
    }

    //跳转到编辑页面
    @RequestMapping("/workbench/transaction/editTran.do")
    public String editTran(String id,Model model){
        //提供要编辑的交易对象
        Tran tranDetail = tranService.queryTranForDetailById(id);
        Tran tran = tranService.queryByPrimaryKey(id);
        //提供所有的用户
        List<User> userList = userService.queryAllUsers();
        //提供所有交易阶段
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //提供所有交易类型
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        //提供所有交易来源
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //提供可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tranDetail.getStage());
        tranDetail.setPossibility(possibility);

        model.addAttribute("tranDetail", tranDetail);
        model.addAttribute("tran", tran);
        model.addAttribute("userList", userList);
        model.addAttribute("stageList", stageList);
        model.addAttribute("transactionTypeList", transactionTypeList);
        model.addAttribute("sourceList", sourceList);
        return "workbench/transaction/edit";
    }

    //保存编辑内容
    @RequestMapping("/workbench/transaction/saveEditTran.do")
    @ResponseBody
    public Object saveEditTran(Tran tran,String customerName,HttpSession session){
        User user = (User)session.getAttribute(Constant.SESSION_USER);
        tran.setEditBy(user.getId());
        tran.setEditTime(DateUtils.formatDateTime(new Date()));

        Map<String,Object> map = new HashMap<>();
        map.put("tran", tran);
        map.put("customerName", customerName);
        map.put("sessionUser", user);

        ReturnObject returnObject = new ReturnObject();

        try {
            tranService.saveEditTran(map);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("修改交易失败");
        }

        return  returnObject;
    }

    //删除交易
    @RequestMapping("/workbench/tran/deleteTranByIds.do")
    @ResponseBody
    public Object deleteTranByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.deleteTranByIds(id);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除交易失败");
        }
        return returnObject;
    }
}
