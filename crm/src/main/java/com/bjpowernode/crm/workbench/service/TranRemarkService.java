package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TranRemark;

import java.util.List;

/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-10
 * @公司： www.bjpowernode.com
 * @描述：
 */
public interface TranRemarkService {
    List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);

    int saveCreateTranRemark(TranRemark remark);

    int deleteTranRemarkById(String id);

    int saveEditTranRemark(TranRemark remark);
}
