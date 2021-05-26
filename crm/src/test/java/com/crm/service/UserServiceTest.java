package com.crm.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 罗健
 * 2021/4/24
 */
public class UserServiceTest extends BaseTest {

    @Autowired
    UserService userService;

    @Test
    public void testSelectById(){
        User user = userService.selectUserById("06f5fc056eac41558a964f96daa7f27c");
        System.out.println(user.getName());
    }
}
