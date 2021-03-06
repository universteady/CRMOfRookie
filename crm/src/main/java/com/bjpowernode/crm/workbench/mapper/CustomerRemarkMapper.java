package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    int insert(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    int insertSelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    CustomerRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    int updateByPrimaryKeySelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Sat Jun 06 16:07:31 CST 2020
     */
    int updateByPrimaryKey(CustomerRemark record);

    /**
    * 批量保存创建的客户备注
    */
    int insertCustomerRemarkByList(List<CustomerRemark> remarkList);

    //批量删除备注
    int deleteCustomerRemarkByCustomerIds(String[] clueIds);

    //根据客户id查询所有备注
    List<CustomerRemark> selectCustomerRemarkForDetailByCustomerId(String customerId);

    /**
     * 保存创建的客户备注
     */
    int insertCustomerRemark(CustomerRemark remark);

    /**
     * 根据id删除客户备注
     */
    int deleteCustomerRemarkById(String id);

    /**
     * 保存修改的客户备注
     */
    int updateCustomerRemark(CustomerRemark remark);
}