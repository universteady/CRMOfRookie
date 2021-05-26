package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

/**
 * 罗健
 * 2021/5/13
 */
public interface CustomerRemarkService {
    List<CustomerRemark> queryCustomerRemarkForDetailByCustomerId(String customerId);

    int saveCreateCustomerRemark(CustomerRemark remark);

    int deleteCustomerRemarkById(String id);

    int saveEditCustomerRemark(CustomerRemark remark);
}
