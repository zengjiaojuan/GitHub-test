package com.phb.puhuibao.dao;

import org.springframework.stereotype.Repository;

import com.idp.pub.dao.impl.DefaultBaseDao;
import com.phb.puhuibao.entity.Bank;

@Repository("bankDao")
public class BankDao extends DefaultBaseDao<Bank,String> {
	public String getNamespace() {
		return Bank.class.getName();
	}

}
