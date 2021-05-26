package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 罗健
 * 2021/5/13
 */
@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private TranService tranService;
    @Autowired
    private ContactsService contactsService;

    @RequestMapping("/workbench/customer/index.do")
    public String index(Model model){
        //提供所有用户列表
        List<User> userList = userService.queryAllUsers();

        model.addAttribute("userList", userList);
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/queryCustomerForPageByCondition.do")
    @ResponseBody
    public Object queryCustomerForPageByCondition(int pageNo, int pageSize, String name, String owner, String phone, String website){
        Map<String, Object> map = new HashMap<>();
        int beginNo = (pageNo - 1) * pageSize;
        map.put("beginNo", beginNo);
        map.put("pageSize", pageSize);
        map.put("name", name);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("website", website);

        //获取满足条件的所有的顾客
        List<Customer> customerList = customerService.queryCustomerForPageByCondition(map);
        //获取满足条件的顾客条数
        long totalRows = customerService.queryCountOfCustomerByCondition(map);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("customerList", customerList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    @RequestMapping("/workbench/customer/saveCreateCustomer.do")
    @ResponseBody
    public Object saveCreateCustomer(Customer customer, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        int ret = 0;

        customer.setId(UUIDUtils.getUUID());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = customerService.saveCreateCustomer(customer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加顾客失败");
            }
            return returnObject;
        }
    }

    @RequestMapping("/workbench/customer/editCustomer.do")
    @ResponseBody
    public Object editCustomer(String id){
        Customer customer = customerService.queryCustomerById(id);
        return customer;
    }

    @RequestMapping("/workbench/customer/saveEditCustomer.do")
    @ResponseBody
    public Object saveEditCustomer(Customer customer,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        int ret = 0;

        User user = (User) session.getAttribute(Constant.SESSION_USER);
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = customerService.updateByPrimaryKeySelective(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新顾客失败");
            }

            return returnObject;
        }
    }

    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    @ResponseBody
    public Object deleteCustomerByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            customerService.deleteCustomerByIds(id);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除顾客失败");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/customer/detailCustomer.do")
    public String detailCustomer(String id,Model model){
        //顾客详情
        Customer customer=customerService.queryCustomerForDetailById(id);
        //顾客备注
        List<CustomerRemark> remarkList=customerRemarkService.queryCustomerRemarkForDetailByCustomerId(id);

        //关联的交易
        List<Tran> tranList = tranService.queryTranForDetailByCustomerId(id);
        //给每个交易的可能性赋值
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        for (Tran tran : tranList) {
            String possibility = bundle.getString(tran.getStage());
            tran.setPossibility(possibility);
        }

        //关联的联系人
        List<Contacts> contactsList = contactsService.queryContactsForDetailByCustomerId(id);

        model.addAttribute("customer", customer);
        model.addAttribute("remarkList", remarkList);
        model.addAttribute("tranList", tranList);
        model.addAttribute("contactsList", contactsList);

        return "workbench/customer/detail";

    }

    //删除关联的交易
    @RequestMapping("workbench/customer/deleteTranById.do")
    @ResponseBody
    public Object deleteTranById(String id){
        String show = id;
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.deleteTranById(id);
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除交易失败");
        }
        return  returnObject;
    }
}
