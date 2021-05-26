package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.mapper.DicTypeMapper;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 罗健
 * 2021/4/26
 */
@Service
public class DicTypeServiceImpl implements DicTypeService {

    @Autowired
    DicTypeMapper dicTypeMapper;

    @Override
    public List<DicType> queryAllDicType() {
        return dicTypeMapper.selectAllDicType();
    }

    @Override
    public DicType queryDicTypeByCode(String code) {
        return dicTypeMapper.selectDicTypeByCode(code);
    }

    @Override
    public int deleteDicType(String[] codes) {
        return dicTypeMapper.deleteDicType(codes);
    }

    @Override
    public int updateDicType(DicType dicType) {
        return dicTypeMapper.updateDicType(dicType);
    }

    @Override
    public int insertDicType(DicType dicType) {
        return dicTypeMapper.insertDicType(dicType);
    }
}
