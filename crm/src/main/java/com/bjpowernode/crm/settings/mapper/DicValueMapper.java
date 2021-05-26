package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicValueMapper {

    //查询所有字典值
    List<DicValue> selectAllDicValue();

    //查询单个字典值
    DicValue selectDicValue(String id);

    //增加字典值
    int insertDicValue(DicValue dicValue);

    //删除字典值(单个或批量)
    int deleteDicType(String[] ids);

    //更改字典值
    int updateDicType(DicValue dicValue);

    //部分更改字典值
    int updateDicTypeSelective(DicValue dicValue);

    //按页面查找字典值
    List<DicValue> selectDicValueForPage(Map<String,Object> map);

    //查找字典值个数
    long selectCountOfDicValueForPage();

    /**
     * 根据typeCode查询数据字典值
     */
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}