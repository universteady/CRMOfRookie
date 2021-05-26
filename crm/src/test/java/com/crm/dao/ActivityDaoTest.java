package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/29
 */
public class ActivityDaoTest  extends BaseTest {
    @Autowired
    ActivityMapper activityMapper;

    @Test
    public void testSelectActivityForPageByCondition(){
        Map<String,Object> map = new HashMap<>();
        map.put("beginNo", 0);
        map.put("pageSize", 6);
        map.put("startDate", "2021-04-15");
        map.put("endDate", "2021-04-27");

        List<Activity> activityList = activityMapper.selectActivityForPageByCondition(map);
        System.out.println(activityList.size());
    }
}
