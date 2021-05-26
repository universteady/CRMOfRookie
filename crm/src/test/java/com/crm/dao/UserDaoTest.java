package com.crm.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/24
 */
public class UserDaoTest extends BaseTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testSelectUserById(){
        User user = userMapper.selectByPrimaryKey("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(user.getName());
    }

    @Test
    public void testSelectUserByLoginActAndLoginPwd(){
        Map map = new HashMap();
        map.put("loginAct", "ls");
        map.put("loginPwd", "44ba5ca65651b4f36f1927576dd35436");

        User user = userMapper.selectUserByLoginActAndPwd(map);
        System.out.println(user.getName());
    }

    @Test
    public void testSelectAllUsers(){
        List<User> users = userMapper.selectAllUsers();
        System.out.println(users.size());
    }
}
