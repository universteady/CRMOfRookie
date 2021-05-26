/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-03
 * @公司： www.bjpowernode.com
 * @描述：
 */
package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>NAME: ClueRemarkServiceImpl</p>
 * @author Administrator
 * @date 2020-06-03 17:27:33
 */
@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }

    @Override
    public int saveCreateClueRemark(ClueRemark remark) {
        return clueRemarkMapper.insertClueRemark(remark);
    }

    @Override
    public int deleteClueRemarkById(String id) {
        return clueRemarkMapper.deleteClueRemarkById(id);
    }

    @Override
    public int deleteClueRemarkByClueIds(String[] clueIds) {
        return clueRemarkMapper.deleteClueRemarkByClueIds(clueIds);
    }

    @Override
    public int saveEditClueRemark(ClueRemark remark) {
        return clueRemarkMapper.updateClueRemark(remark);
    }
}
