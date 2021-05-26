package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 罗健
 * 2021/4/26
 */
public interface DicTypeService {
    List<DicType> queryAllDicType();
    DicType queryDicTypeByCode(String code);
    int deleteDicType(String[] codes);
    int updateDicType(DicType dicType);
    int insertDicType(DicType dicType);
}
