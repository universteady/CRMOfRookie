package com.crm.dao;

import com.bjpowernode.crm.commons.utils.UUIDUtils;
import org.junit.Test;

/**
 * 罗健
 * 2021/5/6
 */
public class UUIDUtilsTest {
    @Test
    public void testUUID(){
        String uuid = UUIDUtils.getUUID();
        System.out.println(uuid);
    }
}
