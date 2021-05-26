/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-09
 * @公司： www.bjpowernode.com
 * @描述：
 */
package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>NAME: TranServiceImpl</p>
 *
 * @author Administrator
 * @date 2020-06-09 16:39:20
 */
@Service("tranService")
public class TranServiceImpl implements TranService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;
    @Autowired
    private DicValueMapper dicValueMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateTran(Map<String, Object> map) {
        Tran tran = (Tran) map.get("tran");
        String customerId = tran.getCustomerId();
        String customerName = (String) map.get("customerName");
        User user = (User) map.get("sessionUser");

        //如果需要创建客户
        if (customerId == null || customerId.trim().length() == 0) {
            Customer customer = new Customer();
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());

            int ret = customerMapper.insertCustomer(customer);

            //把customer的id设置到tran对象中
            tran.setCustomerId(customer.getId());
        }

        //保存交易
        tranMapper.insertTran(tran);

        //保存交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);

        return 0;
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }

    @Override
    public List<Tran> queryTranForPageByCondition(Map<String, Object> map) {
        return tranMapper.selectTranForPageByCondition(map);
    }

    @Override
    public long queryCountOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTranByCondition(map);
    }

    @Override
    public Tran queryByPrimaryKey(String id) {
        return tranMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveEditTran(Map<String, Object> map) {
        Tran tran = (Tran) map.get("tran");
        String customerId = tran.getCustomerId();
        String customerName = (String) map.get("customerName");
        User user = (User) map.get("sessionUser");
        boolean isChange = false;

        //如果需要创建客户
        if (customerId == null || customerId.trim().length() == 0) {
            Customer customer = new Customer();
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());

            int ret = customerMapper.insertCustomer(customer);

            //把customer的id设置到tran对象中
            tran.setCustomerId(customer.getId());
        }
        //更新前判断交易阶段是否改变
        Tran priorTran = tranMapper.selectByPrimaryKey(tran.getId());
        String priorStage = priorTran.getStage();
        if (!priorStage.equals(tran.getStage())) {
            isChange = true;
        }


        //更新交易
        tranMapper.updateByPrimaryKeySelective(tran);

        //添加交易历史记录，如果交易阶段没有改变且交易阶段stage为stageList的最后两个阶段,则不添加(为了获取交易失败前的最后交易阶段)
        List<DicValue> stageList = dicValueMapper.selectDicValueByTypeCode("stage");
        DicValue failStage1 = stageList.get(stageList.size() - 2);
        DicValue failStage2 = stageList.get(stageList.size() - 1);

        String curStageId = tran.getStage();
        String failId1 = failStage1.getId();
        String failId2 = failStage2.getId();


        if (isChange) {
            if (!(failId1.equals(curStageId) || failId2.equals(curStageId))) {
                TranHistory tranHistory = new TranHistory();
                tranHistory.setCreateBy(user.getId());
                tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
                tranHistory.setExpectedDate(tran.getExpectedDate());
                tranHistory.setId(UUIDUtils.getUUID());
                tranHistory.setMoney(tran.getMoney());
                tranHistory.setStage(tran.getStage());
                tranHistory.setTranId(tran.getId());
                tranHistoryMapper.insertTranHistory(tranHistory);
            }
        }

    }

    @Override
    public void deleteTranByIds(String[] ids) {
        //删除交易关联交易历史表的相关信息
        tranHistoryMapper.deleteTranHistoryByTranIds(ids);
        //删除交易的所有备注
        tranRemarkMapper.deleteTranRemarkByTranIds(ids);
        //删除交易
        tranMapper.deleteTranByIds(ids);
    }

    @Override
    public List<Tran> queryTranForDetailByCustomerId(String customerId) {
        return tranMapper.selectTranForDetailByCustomerId(customerId);
    }

    @Override
    public void deleteTranById(String id) {

        //删除交易关联交易历史表的相关信息
        tranHistoryMapper.deleteTranHistoryByTranId(id);
        //删除交易的所有备注
        tranRemarkMapper.deleteTranRemarkByTranId(id);
        //删除交易
        tranMapper.deleteByPrimaryKey(id);

    }
}
