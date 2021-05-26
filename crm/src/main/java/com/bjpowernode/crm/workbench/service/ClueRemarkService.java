package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-03
 * @公司： www.bjpowernode.com
 * @描述：
 */
public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    int saveCreateClueRemark(ClueRemark remark);

    int deleteClueRemarkById(String id);

    int deleteClueRemarkByClueIds(String[] clueIds);

    int saveEditClueRemark(ClueRemark remark);
}
