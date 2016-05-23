package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.BorrowerManagement;

@Repository("borrowerManagementDao")
public class BorrowerManagementDao extends DefaultBaseDao<BorrowerManagement, String> {
	public String getNamespace() {
		return BorrowerManagement.class.getName();
	}

}
