package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

public interface DicTypeMapper {

    //显示所有字典类型列表
    List<DicType> selectAllDicType();

    //根据编号查询字典类型
    DicType selectDicTypeByCode(String code);

    //删除字典类型(单个和批量)
    int deleteDicType(String[] codes);

    //更改字典类型
    int updateDicType(DicType dicType);

    //增加字典类型
    int insertDicType(DicType dicType);


}