package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-09
 * @公司： www.bjpowernode.com
 * @描述：
 */
public interface TranService {
    int saveCreateTran(Map<String, Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

    List<Tran> queryTranForPageByCondition(Map<String, Object> map);

    long queryCountOfTranByCondition(Map<String, Object> map);

    Tran queryByPrimaryKey(String id);

    void saveEditTran(Map<String, Object> map);

    void deleteTranByIds(String[] ids);

    List<Tran> queryTranForDetailByCustomerId(String customerId);

    void deleteTranById(String id);
}
