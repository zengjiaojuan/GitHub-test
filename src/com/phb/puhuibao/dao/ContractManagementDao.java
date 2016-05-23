package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.ContractManagement;

@Repository("contractManagementDao")
public class ContractManagementDao extends DefaultBaseDao<ContractManagement, String> {
	public String getNamespace() {
		return ContractManagement.class.getName();
	}

}
