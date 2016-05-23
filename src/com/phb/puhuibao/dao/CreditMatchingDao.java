package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.CreditMatching;

@Repository("creditMatchingDao")
public class CreditMatchingDao extends DefaultBaseDao<CreditMatching, String> {
	public String getNamespace() {
		return CreditMatching.class.getName();
	}

}
