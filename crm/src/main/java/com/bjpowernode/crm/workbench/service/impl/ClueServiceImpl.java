package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }


    @Override
    public List<Clue> queryClueForPageByCondition(Map<String, Object> map) {
        return clueMapper.selectClueForPageByCondition(map);
    }

    @Override
    public long queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int updateByPrimaryKey(Clue clue) {
        return clueMapper.updateByPrimaryKey(clue);
    }

    @Override
    public void deleteClueByIds(String[] ids) {
        //删除线索关联市场活动表的相关信息
        clueActivityRelationMapper.deleteClueActivityRelationByClueIds(ids);
        //删除线索的所有备注
        clueRemarkMapper.deleteClueRemarkByClueIds(ids);
        //删除线索
         clueMapper.deleteClueByIds(ids);
    }

    @Override
    public int updateByPrimaryKeySelective(Clue clue) {
        return clueMapper.updateByPrimaryKeySelective(clue);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        User user = (User)map.get("user");
        String clueId = (String)map.get("clueId");
        String isCreateTran = (String)map.get("isCreateTran");

        //根据clueId获得clue
        Clue clue = clueMapper.selectClueById(clueId);

        //1.将线索表信息拆分为顾客customer和联系人contacts表

        //新建顾客和联系人对象
        Customer customer = new Customer();
        Contacts contacts = new Contacts();

        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());

        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullName(clue.getFullName());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        //将顾客和联系人对象插入数据库
        customerMapper.insertCustomer(customer);
        contactsMapper.insertContacts(contacts);

        //2.将线索备注转换到顾客备注表和联系人备注表
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        if (clueRemarkList != null && clueRemarkList.size()>0) {
            CustomerRemark cusr = null;
            ContactsRemark conr = null;
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();

            for (ClueRemark cr : clueRemarkList) {
                cusr = new CustomerRemark();
                conr = new ContactsRemark();

                cusr.setId(UUIDUtils.getUUID());
                cusr.setNoteContent(cr.getNoteContent());
                cusr.setCreateBy(cr.getCreateBy());
                cusr.setCreateTime(cr.getCreateTime());
                cusr.setEditBy(cr.getEditBy());
                cusr.setEditTime(cr.getEditTime());
                cusr.setEditFlag(cr.getEditFlag());
                cusr.setCustomerId(customer.getId());

                conr.setId(UUIDUtils.getUUID());
                conr.setNoteContent(cr.getNoteContent());
                conr.setCreateBy(cr.getCreateBy());
                conr.setCreateTime(cr.getCreateTime());
                conr.setEditBy(cr.getEditBy());
                conr.setEditTime(cr.getEditTime());
                conr.setEditFlag(cr.getEditFlag());
                conr.setContactsId(contacts.getId());

                customerRemarkList.add(cusr);
                contactsRemarkList.add(conr);
            }

            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
        }

        //3.将线索关联的市场活动转换到联系人关联的市场活动表
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

       if(carList != null && carList.size()>0){
           ContactsActivityRelation coar = null;
           List<ContactsActivityRelation> coarList = new ArrayList<>();

           for (ClueActivityRelation car : carList) {
               coar = new ContactsActivityRelation();
               coar.setId(UUIDUtils.getUUID());
               coar.setContactsId(contacts.getId());
               coar.setActivityId(car.getActivityId());

               coarList.add(coar);
           }
           contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
       }

       //4.如果isCreateTran为true,则新建一个交易
        if("true".equals(isCreateTran)){
            Tran tran = new Tran();

            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String) map.get("amountOfMoney"));
            tran.setName((String)map.get("tradeName"));
            tran.setExpectedDate((String)map.get("expectedClosingDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String)map.get("stage"));
            tran.setActivityId((String)map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));

            tranMapper.insertTran(tran);

            //如果新建了一个交易，则新建一个交易记录（下个模块做）,还要将线索的备注转换到交易备注
            if (clueRemarkList != null && clueRemarkList.size()>0) {
                TranRemark tr = null;
                List<TranRemark> trList = new ArrayList<>();

                for (ClueRemark cr : clueRemarkList) {
                    tr = new TranRemark();

                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditby(cr.getEditBy());
                    tr.setEditTime(cr.getEditTime());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setTranId(tran.getId());

                    trList.add(tr);
                }

                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }

        //5.删除线索关联市场活动表相关信息
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //6.删除该线索的所有备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //7.删除该线索
        clueMapper.deleteClueById(clueId);

    }
}
