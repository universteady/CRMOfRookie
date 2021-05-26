package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/26
 */
public interface DicValueService {
    List<DicValue> selectAllDicValue();
    DicValue selectDicValue(String id);
    int addDicValue(DicValue dicValue);
    int deleteDicValue(String[] ids);
    int updateDicValue(DicValue dicValue);
    int updateDicValueSelective(DicValue dicValue);
    List<DicValue> selectDicValueForPage(Map<String,Object> map);
    long selectCountOfDicValueForPage();
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
