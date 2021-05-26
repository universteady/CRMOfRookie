package com.crm.dao;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/5/9
 */
public class ClueServiceTest extends BaseTest {

    @Autowired
    private ClueService clueService;

    @Test
    public void testQueryClueByCondition(){
        Map<String,Object> map = new HashMap<>();
        map.put("beginNo", 0);
        map.put("pageSize", 3);
        map.put("fullName", "王");
        List<Clue> clueList = clueService.queryClueForPageByCondition(map);
        System.out.println(clueList.size());
    }

    @Test
    public void testQueryClueForDetailById(){
        String id = "35f2462415df46e3a1512aeb97c6cdb9";
        Clue clue=clueService.queryClueForDetailById(id);
        System.out.println(clue);
    }
}
