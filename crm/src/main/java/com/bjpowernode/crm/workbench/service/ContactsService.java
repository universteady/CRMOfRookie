package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * 罗健
 * 2021/5/14
 */
public interface ContactsService {

    List<Contacts> queryContactsForDetailByName(String fullName);

    List<Contacts> queryContactsForDetailByCustomerId(String customerId);
}
