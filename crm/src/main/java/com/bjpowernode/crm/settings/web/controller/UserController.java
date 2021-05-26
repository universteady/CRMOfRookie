package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 罗健
 * 2021/4/24
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    //跳转到登陆界面
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request){
        //验证cookie,如果之前开启了免登录，则直接跳转到/workbench/index
        Cookie[] cookies = request.getCookies();
        String loginAct = null;
        String loginPwd = null;
        Map<String,Object> map = new HashMap<>();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("loginAct".equals(name)){
                    loginAct = cookie.getValue();
                }
                if("loginPwd".equals(name)){
                    loginPwd = cookie.getValue();
                }
            }
        }

        if(loginAct != null && loginPwd != null){
            map.put("loginAct", loginAct);
            map.put("loginPwd", MD5Util.getMD5(loginPwd));
            User user = userService.queryUserByLoginActAndPwd(map);
            request.getSession().setAttribute(Constant.SESSION_USER, user);

            return "forward:/workbench/index.do";
        }else{

            //如果之前没有开启免登录，则跳转到登陆页面
            return "/settings/qx/user/login";
        }

    }

    //登录验证
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){

        ReturnObject returnObject = new ReturnObject();
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));

        System.out.println(request.getRemoteAddr());

        User user = userService.queryUserByLoginActAndPwd(map);
        //user不存在
        if(user == null){
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else{
            //账户超期
            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账户已超期");

                //用户已锁定
            }else if("0".equals(user.getLockState())){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("用户已锁定");

                //ip被禁止访问
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip被禁止访问");

                //登陆成功
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Constant.SESSION_USER, user);

                //如果开启了免登录
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd", loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);

                    //没有开启免登录
                }else{
                    Cookie c1 = new Cookie("loginAct", null);
                    c1.setMaxAge(0);
                    response.addCookie(c1);

                    Cookie c2 = new Cookie("loginPwd", null);
                    c1.setMaxAge(0);
                    response.addCookie(c2);
                }

            }
        }

        return returnObject;
    }

    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        Cookie c1 = new Cookie("loginAct", null);
        c1.setMaxAge(0);
        response.addCookie(c1);

        Cookie c2 = new Cookie("loginPwd", null);
        c1.setMaxAge(0);
        response.addCookie(c2);

        session.invalidate();

        return "/settings/qx/user/login";
    }
}
