package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

/**
 * @项目名：crm-project
 * @创建人： Administrator
 * @创建时间： 2020-06-09
 * @公司： www.bjpowernode.com
 * @描述：
 */
public interface CustomerService {
    List<Customer> queryCustomerByName(String name);

    List<Customer> queryCustomerForPageByCondition(Map<String, Object> map);

    long queryCountOfCustomerByCondition(Map<String, Object> map);

    int saveCreateCustomer(Customer customer);

    Customer queryCustomerById(String id);

    int updateByPrimaryKeySelective(Customer customer);

    void deleteCustomerByIds(String[] ids);

    Customer queryCustomerForDetailById(String id);
}
