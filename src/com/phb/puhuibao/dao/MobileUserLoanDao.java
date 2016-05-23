package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.MobileUserLoan;

@Repository("mobileUserLoanDao")
public class MobileUserLoanDao extends DefaultBaseDao<MobileUserLoan,String> {
	public String getNamespace() {
		return MobileUserLoan.class.getName();
	}

}
