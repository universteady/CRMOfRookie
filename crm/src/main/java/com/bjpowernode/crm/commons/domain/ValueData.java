package com.bjpowernode.crm.commons.domain;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * 罗健
 * 2021/4/30
 */
public class ValueData {
    private List<DicValue> dicValueList;
    private Long totalRows;

    public List<DicValue> getDicValueList() {
        return dicValueList;
    }

    public void setDicValueList(List<DicValue> dicValueList) {
        this.dicValueList = dicValueList;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
    }
}
