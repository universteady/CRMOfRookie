package com.crm.dao;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.crm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 罗健
 * 2021/4/27
 */
public class DicTypeDaoTest  extends BaseTest {

    @Autowired
    DicTypeMapper dicTypeMapper;

    @Test
    public void testInsertDicType(){
        DicType dicType = new DicType();
        dicType.setCode("11");
        dicType.setName("11");
        dicType.setDescription("111");

        int i = dicTypeMapper.insertDicType(dicType);
        System.out.println(i);
    }
}
