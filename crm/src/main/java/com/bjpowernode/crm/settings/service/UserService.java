package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/24
 */
public interface UserService {
    public User selectUserById(String id);

    //根据用户名和密码查询用户
    public User queryUserByLoginActAndPwd(Map<String,Object> map);

    //查询所有用户
    public List<User> queryAllUsers();
}
