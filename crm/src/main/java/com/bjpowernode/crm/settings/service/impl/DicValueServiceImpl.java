package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/26
 */
@Service
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> selectAllDicValue() {
        return dicValueMapper.selectAllDicValue();
    }

    @Override
    public DicValue selectDicValue(String id) {
        return dicValueMapper.selectDicValue(id);
    }

    @Override
    public int addDicValue(DicValue dicValue) {
        return dicValueMapper.insertDicValue(dicValue);
    }

    @Override
    public int deleteDicValue(String[] ids) {
        return dicValueMapper.deleteDicType(ids);
    }

    @Override
    public int updateDicValue(DicValue dicValue) {
        return dicValueMapper.updateDicType(dicValue);
    }

    @Override
    public int updateDicValueSelective(DicValue dicValue) {
        return dicValueMapper.updateDicTypeSelective(dicValue);
    }

    @Override
    public List<DicValue> selectDicValueForPage(Map<String, Object> map) {
        return dicValueMapper.selectDicValueForPage(map);
    }

    @Override
    public long selectCountOfDicValueForPage() {
        return dicValueMapper.selectCountOfDicValueForPage();
    }

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
