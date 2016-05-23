package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.LoanItem;

@Repository("loanItemDao")
public class LoanItemDao extends DefaultBaseDao<LoanItem,String> {
	public String getNamespace() {
		return LoanItem.class.getName();
	}

}
