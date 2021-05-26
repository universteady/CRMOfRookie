package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Cost;

import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/30
 */
public interface ActivityService {
    int saveActivity(Activity activity);
    List<Activity> queryActivityForPageByCondition(Map<String, Object> map);
    long queryCountOfActivityByCondition(Map<String, Object> map);
    Activity queryActivityById(String id);
    int updateActivity(Activity activity);
    int deleteActivityByIds(String[] ids);
    List<Activity> queryAllActivityForDetail();
    List<Activity> queryActivityForDetailByIds(String[] ids);
    int saveActivityByList(List<Activity> activityList);
    Activity queryActivityForDetailById(String id);
    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityNoBoundByClueId(Map<String,Object> map);

    List<Activity> queryActivityForDetailByName(String name);

    List<Cost> queryCostOfActivity();
}
