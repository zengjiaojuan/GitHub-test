package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.CustomerManagement;

@Repository("customerManagementDao")
public class CustomerManagementDao extends DefaultBaseDao<CustomerManagement, String> {
	public String getNamespace() {
		return CustomerManagement.class.getName();
	}

}
