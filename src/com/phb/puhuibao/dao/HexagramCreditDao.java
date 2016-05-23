package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.HexagramCredit;
 

@Repository("hexagramCreditDao")
public class HexagramCreditDao extends DefaultBaseDao<HexagramCredit,String>{
	@Override
	public String getNamespace() {
		return HexagramCredit.class.getName();
	}
}

