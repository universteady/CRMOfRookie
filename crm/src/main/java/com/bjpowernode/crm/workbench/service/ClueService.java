package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvert(Map<String, Object> map);

    List<Clue> queryClueForPageByCondition(Map<String, Object> map);

    long queryCountOfClueByCondition(Map<String, Object> map);

    Clue queryClueById(String id);

    int updateByPrimaryKey(Clue clue);

    void deleteClueByIds(String[] ids);

    int updateByPrimaryKeySelective(Clue clue);
}
