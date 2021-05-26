package com.crm.dao;

import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 罗健
 * 2021/4/26
 */
public class DicValueDaoTest extends BaseTest {

    @Autowired
    DicValueMapper dicValueMapper;

    @Test
    public void testInsertDicValue(){
        DicValue dicValue = new DicValue();
        dicValue.setOrderNo("5");
        dicValue.setText("测试用");
        dicValue.setValue("5");
        dicValue.setTypeCode("test");
        dicValue.setId(UUIDUtils.getUUID());

        int i = dicValueMapper.insertDicValue(dicValue);
        System.out.println(i);
    }
}
