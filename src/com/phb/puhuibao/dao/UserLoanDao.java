package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.UserLoan;

@Repository("userLoanDao")
public class UserLoanDao extends DefaultBaseDao<UserLoan,String> {
	public String getNamespace() {
		return UserLoan.class.getName();
	}
}
